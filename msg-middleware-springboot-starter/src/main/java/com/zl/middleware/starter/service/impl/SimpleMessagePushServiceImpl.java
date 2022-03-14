package com.zl.middleware.starter.service.impl;

import com.zl.middleware.sdk.def.MessageEntity;
import com.zl.middleware.sdk.def.RenderStrategy;
import com.zl.middleware.sdk.def.SystemTopic;
import com.zl.middleware.sdk.service.MessagePushService;
import com.zl.middleware.sdk.service.impl.MessagePushServiceImpl;
import com.zl.middleware.starter.config.SystemEnvironmentConfigProperty;
import com.zl.middleware.starter.service.SimpleMessagePushService;
import okhttp3.Callback;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 二次封装{@link com.zl.middleware.sdk.service.MessagePushService}接口，简化了方法参数
 *
 * @author zl
 * @since 2021/11/23 16:28
 */
public class SimpleMessagePushServiceImpl extends MessagePushServiceImpl implements SimpleMessagePushService {

    @Autowired
    private SystemEnvironmentConfigProperty property;

    /**
     * 直接推送到中间件的消息直推接口
     *
     * @param messageEntity 消息实体对象
     * @see MessagePushService#directlyPush(String, MessageEntity)
     */
    @Override
    public <M extends MessageEntity> Response directlyPush(M messageEntity) throws IOException {
        return this.directlyPush(property.getPush().getUri(), messageEntity);
    }

    /**
     * 直接推送到中间件的消息直推接口
     *
     * @param messageEntity 消息实体对象
     * @see MessagePushService#directlyPush(String, MessageEntity)
     */
    @Override
    public <M extends MessageEntity> Response directlyPushArray(List<M> messageEntity) throws IOException {
        return this.directlyPushArray(property.getPush().getUri(), messageEntity);
    }

    /**
     * 直接推送到中间件的消息直推接口
     *
     * @param messageEntity  消息实体对象
     * @param renderStrategy 渲染策略
     * @see MessagePushService#directlyPushAsync(String, MessageEntity, Callback)
     */
    @Override
    public <M extends MessageEntity> Response directlyPush(M messageEntity, RenderStrategy renderStrategy) throws IOException {
        return this.directlyPush(property.getPush().getUri(), messageEntity, renderStrategy);
    }

    /**
     * 直接推送到中间件的消息直推接口
     *
     * @param messageEntity  消息实体对象
     * @param renderStrategy 渲染策略
     * @see MessagePushService#directlyPush(String, MessageEntity, RenderStrategy)
     */
    @Override
    public <M extends MessageEntity> Response directlyPushArray(List<M> messageEntity, RenderStrategy renderStrategy) throws IOException {
        return this.directlyPushArray(property.getPush().getUri(), messageEntity, renderStrategy);
    }

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param messageEntity 消息实体对象
     * @param callback      推送后的回调方法
     * @see MessagePushService#directlyPushAsync(String, MessageEntity, Callback)
     */
    @Override
    public <M extends MessageEntity> void directlyPushAsync(M messageEntity, Callback callback) {
        this.directlyPushAsync(property.getPush().getUri(), messageEntity, callback);
    }

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param messageEntity 消息实体对象
     * @param callback      推送后的回调方法
     * @see MessagePushService#directlyPushAsync(String, MessageEntity, Callback)
     */
    @Override
    public <M extends MessageEntity> void directlyPushArrayAsync(List<M> messageEntity, Callback callback) {
        this.directlyPushArrayAsync(property.getPush().getUri(), messageEntity, callback);
    }

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param messageEntity  消息实体对象
     * @param callback       推送后的回调方法
     * @param renderStrategy 渲染策略
     * @see MessagePushService#directlyPushAsync(String, MessageEntity, Callback, RenderStrategy)
     */
    @Override
    public <M extends MessageEntity> void directlyPushAsync(M messageEntity, Callback callback, RenderStrategy renderStrategy) {
        this.directlyPushAsync(property.getPush().getUri(), messageEntity, callback, renderStrategy);
    }

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param messageEntity  消息实体对象
     * @param callback       推送后的回调方法
     * @param renderStrategy 渲染策略
     * @see MessagePushService#directlyPushAsync(String, MessageEntity, Callback, RenderStrategy)
     */
    @Override
    public <M extends MessageEntity> void directlyPushArrayAsync(List<M> messageEntity, Callback callback, RenderStrategy renderStrategy) {
        this.directlyPushArrayAsync(property.getPush().getUri(), messageEntity, callback, renderStrategy);
    }

    /**
     * 推送至MQ
     *
     * @param messageEntity  消息实体对象
     * @param renderStrategy 渲染策略
     * @see SystemTopic MQ消息主题
     * @see MessagePushService#pushToMQ(String, String, String, SystemTopic, MessageEntity, RenderStrategy)
     */
    @Override
    public <M extends MessageEntity> void pushToMQ(M messageEntity, RenderStrategy renderStrategy) {
        SystemEnvironmentConfigProperty.MQ mqConfigProperty = property.getMq();
        this.pushToMQ(mqConfigProperty.getUser(), mqConfigProperty.getPassword(), mqConfigProperty.getTcpUrl(), mqConfigProperty.getTopic(), messageEntity, renderStrategy);
    }

    /**
     * 推送至MQ
     *
     * @param messageEntity  消息实体对象
     * @param renderStrategy 渲染策略
     * @see SystemTopic MQ消息主题
     * @see MessagePushService#pushToMQ(String, String, String, SystemTopic, MessageEntity, RenderStrategy)
     */
    @Override
    public <M extends MessageEntity> void pushArrayToMQ(List<M> messageEntity, RenderStrategy renderStrategy) {
        SystemEnvironmentConfigProperty.MQ mqConfigProperty = property.getMq();
        this.pushArrayToMQ(mqConfigProperty.getUser(), mqConfigProperty.getPassword(), mqConfigProperty.getTcpUrl(), mqConfigProperty.getTopic(), messageEntity, renderStrategy);
    }
}
