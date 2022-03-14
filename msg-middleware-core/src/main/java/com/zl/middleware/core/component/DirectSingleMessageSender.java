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

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 单条消息发送器。用于<h2>直接</h2>发送指定的JSON Object数据至对应的平台
 *
 * @author zl
 * @since 2021/11/18 10:27
 */
@Slf4j
public class DirectSingleMessageSender extends AbstractSingleMessageSender {

    @Autowired
    private HttpRequestUtil httpRequestUtil;

    @Autowired
    private JsonRender jsonRender;

    @Autowired
    private ParameterRender parameterRender;

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
                        ? jsonRender.renderEssentialObject(getInterfaceId(), getDeclaredJsonFields(), getFieldValues())
                        : jsonRender.renderAllFieldsObject(getInterfaceId(), getDeclaredJsonFields(), getFieldValues());


                //debug log
                log.debug("Push data: {}", json);
                return httpRequestUtil.doExchange(getHttpUrl(), json, headers, getHttpMethod());

            case MediaType.APPLICATION_FORM_URLENCODED_VALUE:
                //直接发送表单请求
                return httpRequestUtil.doExchange(getHttpUrl(),
                        parameterRender.renderParameterMap(getInterfaceId(), getDeclaredJsonFields(), getFieldValues(), getRenderStrategy()),
                        headers, getHttpMethod());

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
    public List<Object> multithreadingSendMessage(Map<String, Object> params) {
        if (params != null) {
            //获取接口List
            List<String> interfaceKeys = Optional.ofNullable(params.get("interfaceKeys")).map(o -> (ArrayList<String>) o).orElse(new ArrayList<>());
            //获取header list
            Map<String, String> headersValue = Optional.ofNullable(params.get("headers")).map(o -> JSON.parseObject(JSON.toJSONString(o), Map.class))
                    .map(map -> (Map<String, String>) map).orElse(new HashMap<>());

            List<Interface> interfaceInfoList = new ArrayList<>();

            for (String interfaceKey : interfaceKeys) {
                //尝试使用接口key查询接口
                Optional.ofNullable(systemApiService.queryInterfaceByKey(interfaceKey))
                        .ifPresent(interfaceInfoList::add);
            }

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

        return new ArrayList<>();
    }

    /**
     * 根据当前数据信息，拼装数据推送线程
     *
     * @param fieldValues   参数Map
     * @param headersValue  http header map
     * @param interfaceInfo 接口信息
     * @param cyclicBarrier 线程同步量
     * @return 数据推送线程
     */
    private Callable<String> assemblyThreadTask(Map<String, Object> fieldValues, Map<String, String> headersValue, Interface interfaceInfo, CyclicBarrier cyclicBarrier) {
        return () -> {
            //构造元数据
            MetaInfo metaInfo = new MetaInfo();
            metaInfo.setFieldValues(fieldValues)
                    .setHeaderValues(headersValue)
                    .setInterfaceInfo(interfaceInfo)
                    .setJsonFields(systemApiService.queryDeclaredFieldsByInterfaceId(interfaceInfo.getId()))
                    .setJsonHeaders(systemApiService.queryDeclaredHeadersByInterfaceId(interfaceInfo.getId()))
                    .setRenderStrategy(Optional.ofNullable(fieldValues.get("renderStrategy"))
                            .map(o -> (String) o)
                            .map(SystemConstant.RenderStrategy::valueOf)
                            .orElse(SystemConstant.RenderStrategy.ALL));

            //设置数据集合
            this.setMetaInfo(metaInfo);
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
