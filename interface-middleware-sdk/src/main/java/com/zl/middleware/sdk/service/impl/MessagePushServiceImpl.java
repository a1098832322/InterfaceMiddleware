package com.zl.middleware.sdk.service.impl;

import com.alibaba.fastjson.JSON;
import com.zl.middleware.sdk.annotation.Alias;
import com.zl.middleware.sdk.annotation.MessageMiddleware;
import com.zl.middleware.sdk.component.ActiveMQWrapperSlim;
import com.zl.middleware.sdk.def.MessageEntity;
import com.zl.middleware.sdk.def.RenderStrategy;
import com.zl.middleware.sdk.def.SystemTopic;
import com.zl.middleware.sdk.service.MessagePushService;
import com.zl.middleware.sdk.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import javax.jms.JMSException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 消息推送服务实现类
 *
 * @author zl
 * @since 2021/11/19 9:49
 */
@Slf4j
public class MessagePushServiceImpl implements MessagePushService {
    /**
     * http请求头
     */
    private Map<String, String> headers;

    /**
     * 本次请求的目标接口key
     */
    private String[] targetInterfaceKeys;

    /**
     * 本次请求的目标系统key
     */
    private String[] targetSystemKeys;

    /**
     * 设置HTTP消息头
     *
     * @param headers HTTP消息头
     */
    @Override
    public MessagePushService setHttpHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 设置请求的目标接口key
     *
     * @param keys 请求的目标接口key
     */
    @Override
    public MessagePushService setTargetInterfaceKeys(String... keys) {
        this.targetInterfaceKeys = keys;
        return this;
    }

    /**
     * 设置请求的目标系统模块key
     *
     * @param keys 目标系统模块key
     */
    @Override
    public MessagePushService setTargetSystemKeys(String... keys) {
        this.targetSystemKeys = keys;
        return this;
    }

    /**
     * 直接推送到中间件的消息直推接口
     *
     * @param url           中间件消息直推接口的完整URL
     * @param messageEntity 消息实体对象
     */
    @Override
    public <M extends MessageEntity> Response directlyPush(String url, M messageEntity) throws IOException {
        return directlyPush(url, messageEntity, RenderStrategy.ALL);
    }

    /**
     * 直接推送到中间件的消息直推接口(推送JSON Array)
     *
     * @param url           中间件消息直推接口的完整URL
     * @param messageEntity 消息实体对象
     */
    @Override
    public <M extends MessageEntity> Response directlyPushArray(String url, List<M> messageEntity) throws IOException {
        return directlyPushArray(url, messageEntity, RenderStrategy.ALL);
    }

    /**
     * 直接推送到中间件的消息直推接口
     *
     * @param url            中间件消息直推接口的完整URL
     * @param messageEntity  消息实体对象
     * @param renderStrategy 渲染策略
     */
    @Override
    public <M extends MessageEntity> Response directlyPush(String url, M messageEntity, RenderStrategy renderStrategy) throws IOException {
        if (renderStrategy == null) {
            renderStrategy = RenderStrategy.ALL;
        }

        if (StringUtils.isNotBlank(url) && messageEntity != null) {
            return HttpUtil.doPostSync(url, buildJsonBody(analysisMessageEntity(messageEntity, renderStrategy)), null);
        }

        throw new IllegalArgumentException("参数不合法！");
    }

    /**
     * 直接推送到中间件的消息直推接口
     *
     * @param url            中间件消息直推接口的完整URL
     * @param messageEntity  消息实体对象
     * @param renderStrategy 渲染策略
     */
    @Override
    public <M extends MessageEntity> Response directlyPushArray(String url, List<M> messageEntity, RenderStrategy renderStrategy) throws IOException {
        if (renderStrategy == null) {
            renderStrategy = RenderStrategy.ALL;
        }

        if (StringUtils.isNotBlank(url) && messageEntity != null && messageEntity.size() > 0) {
            List<Map<String, Object>> jsonMapList = new ArrayList<>();
            for (M m : messageEntity) {
                jsonMapList.add(analysisMessageEntity(m, renderStrategy));
            }

            return HttpUtil.doPostSync(url, buildJsonBody(jsonMapList), null);
        }

        throw new IllegalArgumentException("参数不合法！");
    }

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param url           中间件消息直推接口的完整URL
     * @param messageEntity 消息实体对象
     * @param callback      推送后的回调方法
     */
    @Override
    public <M extends MessageEntity> void directlyPushAsync(String url, M messageEntity, Callback callback) {
        directlyPushAsync(url, messageEntity, callback, RenderStrategy.ALL);
    }

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param url           中间件消息直推接口的完整URL
     * @param messageEntity 消息实体对象
     * @param callback      推送后的回调方法
     */
    @Override
    public <M extends MessageEntity> void directlyPushArrayAsync(String url, List<M> messageEntity, Callback callback) {
        directlyPushArrayAsync(url, messageEntity, callback, RenderStrategy.ALL);
    }

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param url            中间件消息直推接口的完整URL
     * @param messageEntity  消息实体对象
     * @param callback       推送后的回调方法
     * @param renderStrategy 渲染策略
     */
    @Override
    public <M extends MessageEntity> void directlyPushAsync(String url, M messageEntity, Callback callback, RenderStrategy renderStrategy) {
        if (renderStrategy == null) {
            renderStrategy = RenderStrategy.ALL;
        }

        if (StringUtils.isNotBlank(url) && messageEntity != null) {
            HttpUtil.doPostAsync(url, buildJsonBody(analysisMessageEntity(messageEntity, renderStrategy)), null, callback);
            return;
        }

        throw new IllegalArgumentException("参数不合法！");
    }

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param url            中间件消息直推接口的完整URL
     * @param messageEntity  消息实体对象
     * @param callback       推送后的回调方法
     * @param renderStrategy 渲染策略
     */
    @Override
    public <M extends MessageEntity> void directlyPushArrayAsync(String url, List<M> messageEntity, Callback callback, RenderStrategy renderStrategy) {
        if (renderStrategy == null) {
            renderStrategy = RenderStrategy.ALL;
        }

        if (StringUtils.isNotBlank(url) && messageEntity != null && messageEntity.size() > 0) {
            List<Map<String, Object>> jsonMapList = new ArrayList<>();
            for (M m : messageEntity) {
                jsonMapList.add(analysisMessageEntity(m, renderStrategy));
            }
            HttpUtil.doPostAsync(url, buildJsonBody(jsonMapList), null, callback);
            return;
        }

        throw new IllegalArgumentException("参数不合法！");
    }

    /**
     * 推送至MQ
     *
     * @param mqUser         登录MQ的账号
     * @param mqPassword     登录MQ的密码
     * @param mqBrokerUrl    MQ服务的URL地址，例如：tcp://192.168.200.2:61616
     * @param topic          MQ消息主题
     * @param messageEntity  消息实体对象
     * @param renderStrategy 渲染策略
     * @see SystemTopic MQ消息主题
     */
    @Override
    public <M extends MessageEntity> void pushToMQ(String mqUser, String mqPassword, String mqBrokerUrl, SystemTopic topic, M messageEntity, RenderStrategy renderStrategy) {
        if (renderStrategy == null) {
            renderStrategy = RenderStrategy.ALL;
        }

        if (StringUtils.isNotBlank(mqBrokerUrl) && messageEntity != null) {
            try {
                ActiveMQWrapperSlim.getInstance(mqBrokerUrl, mqUser, mqPassword).sendTextMessage(topic.getTopic(), JSON.toJSONString(analysisMessageEntity(messageEntity, renderStrategy)));
            } catch (JMSException e) {
                log.error("推送消息{}至MQ失败！异常:", JSON.toJSONString(messageEntity), e);
            }
            return;
        }

        throw new IllegalArgumentException("参数不合法！");
    }

    /**
     * 推送至MQ
     *
     * @param mqUser         登录MQ的账号
     * @param mqPassword     登录MQ的密码
     * @param mqBrokerUrl    MQ服务的URL地址，例如：tcp://192.168.200.2:61616
     * @param topic          MQ消息主题
     * @param messageEntity  消息实体对象
     * @param renderStrategy 渲染策略
     * @see SystemTopic MQ消息主题
     */
    @Override
    public <M extends MessageEntity> void pushArrayToMQ(String mqUser, String mqPassword, String mqBrokerUrl, SystemTopic topic, List<M> messageEntity, RenderStrategy renderStrategy) {
        if (renderStrategy == null) {
            renderStrategy = RenderStrategy.ALL;
        }

        if (StringUtils.isNotBlank(mqBrokerUrl) && messageEntity != null && messageEntity.size() > 0) {
            List<Map<String, Object>> jsonMapList = new ArrayList<>();
            for (M m : messageEntity) {
                jsonMapList.add(analysisMessageEntity(m, renderStrategy));
            }

            try {
                ActiveMQWrapperSlim.getInstance(mqBrokerUrl, mqUser, mqPassword).sendTextMessage(topic.getTopic(), JSON.toJSONString(jsonMapList));
            } catch (JMSException e) {
                log.error("推送消息{}至MQ失败！异常:", JSON.toJSONString(messageEntity), e);
            }
            return;
        }

        throw new IllegalArgumentException("参数不合法！");
    }

    /**
     * 将参数map构造成json请求体
     *
     * @param paramMap 参数map
     * @param <O>      任意可序列化成JSON的实体类型
     * @return json请求体
     */
    private <O> RequestBody buildJsonBody(O paramMap) {
        return FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , JSON.toJSONString(paramMap));
    }

    /**
     * 拿到实体对应的接口key们
     *
     * @param messageEntity 消息实体
     * @param <M>           消息实体对象类型
     * @return 实体对应的接口key们
     */
    private <M extends MessageEntity> String[] getInterfaceKeys(M messageEntity) {
        MessageMiddleware typeAnnotation = messageEntity.getClass().getAnnotation(MessageMiddleware.class);
        Set<String> legalKeySet = new HashSet<>();
        if (typeAnnotation != null) {
            String[] targetInterfaceKeyDef = typeAnnotation.interfaceKeys();
            //先尝试获取用户指定的请求接口
            if (this.targetInterfaceKeys != null && this.targetInterfaceKeys.length > 0) {
                //对比有多少key是合法的
                for (String key : targetInterfaceKeys) {
                    if (isLegalKey(key, targetInterfaceKeyDef)) {
                        legalKeySet.add(key);
                    }
                }

                return legalKeySet.isEmpty() ? null : legalKeySet.toArray(new String[0]);
            } else {
                //默认推向所有接口
                return targetInterfaceKeyDef;
            }
        }

        return null;
    }

    /**
     * 判断指定的key是否是合法的(不区分大小写)
     *
     * @param key         指定的key
     * @param legalKeyDef 合法key定义集合
     * @return true/false  合法key/不合法key
     */
    private boolean isLegalKey(String key, String[] legalKeyDef) {
        for (String keyDef : legalKeyDef) {
            if (keyDef.equalsIgnoreCase(key)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 解析参数的映射关系
     *
     * @param messageEntity  消息实体
     * @param renderStrategy 渲染策略
     * @param <M>            消息实体对象类型
     * @return 参数的映射关系和值map
     */
    private <M extends MessageEntity> Map<String, Object> analysisMessageEntity(M messageEntity, RenderStrategy renderStrategy) {
        Map<String, Object> paramsMap = new HashMap<>();

        Class<?> clazz = messageEntity.getClass();
        //同时搜索父类
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Alias alias = field.getAnnotation(Alias.class);
                try {
                    Object fieldValue = field.get(messageEntity);
                    if (alias != null) {
                        for (String aliasValue : alias.value()) {
                            //使用定义的别名映射添加参数map
                            paramsMap.put(aliasValue, fieldValue);
                        }
                    } else {
                        //沒有定义映射关系。默认使用参数本名
                        paramsMap.put(field.getName(), fieldValue);
                    }
                } catch (IllegalAccessException e) {
                    log.warn("设置属性:" + field.getName() + " 值时发生异常！当前属性将会被忽略！", e);
                }
            }

            clazz = clazz.getSuperclass();
        }

        //添加接口信息至Map中
        paramsMap.put("interfaceKeys", getInterfaceKeys(messageEntity));
        //添加JSON渲染策略
        paramsMap.put("renderStrategy", renderStrategy.name());
        //如果有，则添加header
        if (headers != null && !headers.isEmpty()) {
            paramsMap.put("headers", headers);
        }

        return paramsMap;
    }
}
