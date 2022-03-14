package com.zl.middleware.core.component;

import com.alibaba.fastjson.JSON;
import com.zl.middleware.core.config.SystemConstant;
import com.zl.middleware.core.dto.MetaInfo;
import com.zl.middleware.core.entity.Interface;
import com.zl.middleware.core.service.SystemApiService;
import com.zl.middleware.core.utils.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 数组消息发送器
 *
 * @author zl
 * @since 2021/12/7 11:31
 */
@Slf4j
public class DirectArrayMessageSender extends AbstractArrayMessageSender {
    @Autowired
    private HttpRequestUtil httpRequestUtil;

    @Autowired
    private JsonRender jsonRender;

    @Autowired
    private HeaderRender headerRender;

    /**
     * 系统服务
     */
    @Autowired
    private SystemApiService systemApiService;

    /**
     * 获取服务器CPU数量
     */
    private static final int PROCESSOR_NUMBER = Runtime.getRuntime().availableProcessors();

    /**
     * 根据服务器CPU数量，创建计算密集型线程池
     */
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(PROCESSOR_NUMBER + 1, 2 * PROCESSOR_NUMBER,
            0, TimeUnit.SECONDS,
            //最大队列
            new ArrayBlockingQueue<>(3 * PROCESSOR_NUMBER),
            // 永远丢弃最老的任务执行最新的任务
            new ThreadPoolExecutor.DiscardPolicy());

    /**
     * 向目标接口推送消息数据
     */
    @Override
    public String sendMessage() throws Exception {
        //构造httpHeaders
        HttpHeaders headers = headerRender.render(getDeclaredHeaders(), getHeadersValue());

        switch (getHttpHeader()) {
            case MediaType
                    .APPLICATION_JSON_VALUE:
            case MediaType.APPLICATION_JSON_UTF8_VALUE:
                //发送json请求
                String json = SystemConstant.RenderStrategy.IGNORE_NO_ESSENTIAL.equals(getRenderStrategy())
                        ? jsonRender.renderEssentialArray(getInterfaceId(), getDeclaredJsonFields(), getFieldValues())
                        : jsonRender.renderAllFieldsArray(getInterfaceId(), getDeclaredJsonFields(), getFieldValues());


                //debug log
                log.debug("Push data: {}", json);
                return httpRequestUtil.doExchange(getHttpUrl(), json, headers, getHttpMethod());

            case MediaType.APPLICATION_FORM_URLENCODED_VALUE:
                //暂不支持post数组，后面有这个需求再说~

            default:
                throw new IllegalAccessException("该请求头标注的请求方式不被允许！");
        }
    }

    /**
     * 多线程直发数组
     *
     * @param params 原始参数
     * @return 响应结果集合
     */
    public List<Object> multithreadingSendMessage(List<Map<String, Object>> params) {
        if (!CollectionUtils.isEmpty(params)) {
            //从典型值中拿到接口信息
            Map<String, Object> typicalValue = params.get(0);

            //获取接口List
            List<String> interfaceKeys = Optional.ofNullable(typicalValue.get("interfaceKeys")).map(o -> (ArrayList<String>) o).orElse(new ArrayList<>());
            //获取header list
            Map<String, String> headersValue = Optional.ofNullable(typicalValue.get("headers")).map(o -> JSON.parseObject(JSON.toJSONString(o), Map.class))
                    .map(map -> (Map<String, String>) map).orElse(new HashMap<>());

            List<Interface> interfaceInfoList = new ArrayList<>();

            for (String interfaceKey : interfaceKeys) {
                //尝试使用接口key查询接口
                Optional.ofNullable(systemApiService.queryInterfaceByKey(interfaceKey))
                        .ifPresent(interfaceInfoList::add);
            }

            //拼装消息体，并将数据提交到发送线程池中
            for (Map<String, Object> param : params) {
                if (param != null) {
                    CyclicBarrier cyclicBarrier = new CyclicBarrier(interfaceInfoList.size());
                    return interfaceInfoList.parallelStream()
                            .map(interfaceInfo -> THREAD_POOL_EXECUTOR.submit(assemblyThreadTask(params, headersValue, interfaceInfo, cyclicBarrier)))
                            .map(stringFuture -> {
                                try {
                                    String responseJson = stringFuture.get();
                                    return JSON.parse(responseJson);
                                } catch (InterruptedException | ExecutionException e) {
                                    log.warn("Get response future failed! ", e);
                                    return null;
                                }
                            })
                            .distinct().collect(Collectors.toList());
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * 根据当前数据信息，拼装数据推送线程
     *
     * @param fieldValues   参数Map集合
     * @param headersValue  http header map
     * @param interfaceInfo 接口信息
     * @param cyclicBarrier 线程同步量
     * @return 数据推送线程
     */
    private Callable<String> assemblyThreadTask(List<Map<String, Object>> fieldValues, Map<String, String> headersValue, Interface interfaceInfo, CyclicBarrier cyclicBarrier) {
        return () -> {
            final List<MetaInfo> metaInfoList = new ArrayList<>();
            fieldValues.forEach(fieldValueMap -> {
                //构造元数据
                MetaInfo metaInfo = new MetaInfo();
                metaInfo.setFieldValues(fieldValueMap)
                        .setHeaderValues(headersValue)
                        .setInterfaceInfo(interfaceInfo)
                        .setJsonFields(systemApiService.queryDeclaredFieldsByInterfaceId(interfaceInfo.getId()))
                        .setJsonHeaders(systemApiService.queryDeclaredHeadersByInterfaceId(interfaceInfo.getId()))
                        .setRenderStrategy(Optional.ofNullable(fieldValueMap.get("renderStrategy"))
                                .map(o -> (String) o)
                                .map(SystemConstant.RenderStrategy::valueOf)
                                .orElse(SystemConstant.RenderStrategy.ALL));

                metaInfoList.add(metaInfo);
            });

            //设置数据集合
            this.setMetaInfoList(metaInfoList);
            try {
                String response = this.sendMessage();
                log.debug("Response: " + response);
                cyclicBarrier.await();
                return response;
            } catch (Exception e) {
                log.error("执行数据推送逻辑发生异常！", e);
                return e.getMessage();
            }
        };
    }
}
