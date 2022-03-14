package com.zl.middleware.starter.service;

import com.zl.middleware.sdk.def.MessageEntity;
import com.zl.middleware.sdk.def.RenderStrategy;
import com.zl.middleware.sdk.def.SystemTopic;
import com.zl.middleware.sdk.service.MessagePushService;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 二次封装{@link com.zl.middleware.sdk.service.MessagePushService}接口，简化了方法参数
 *
 * @author zl
 * @since 2021/11/23 16:27
 */
public interface SimpleMessagePushService extends MessagePushService {
    /**
     * 直接推送到中间件的消息直推接口
     *
     * @param messageEntity 消息实体对象
     * @param <M>           消息实体对象具体类型
     * @see MessagePushService#directlyPush(String, MessageEntity)
     */
    <M extends MessageEntity> Response directlyPush(M messageEntity) throws IOException;

    /**
     * 直接推送到中间件的消息直推接口
     *
     * @param messageEntity 消息实体对象
     * @param <M>           消息实体对象具体类型
     * @see MessagePushService#directlyPush(String, MessageEntity)
     */
    <M extends MessageEntity> Response directlyPushArray(List<M> messageEntity) throws IOException;

    /**
     * 直接推送到中间件的消息直推接口
     *
     * @param messageEntity  消息实体对象
     * @param renderStrategy 渲染策略
     * @param <M>            消息实体对象具体类型
     * @see MessagePushService#directlyPush(String, MessageEntity, RenderStrategy)
     */
    <M extends MessageEntity> Response directlyPush(M messageEntity, RenderStrategy renderStrategy) throws IOException;

    /**
     * 直接推送到中间件的消息直推接口
     *
     * @param messageEntity  消息实体对象
     * @param renderStrategy 渲染策略
     * @param <M>            消息实体对象具体类型
     * @see MessagePushService#directlyPush(String, MessageEntity, RenderStrategy)
     */
    <M extends MessageEntity> Response directlyPushArray(List<M> messageEntity, RenderStrategy renderStrategy) throws IOException;

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param messageEntity 消息实体对象
     * @param callback      推送后的回调方法
     * @param <M>           消息实体对象具体类型
     * @see MessagePushService#directlyPushAsync(String, MessageEntity, Callback)
     */
    <M extends MessageEntity> void directlyPushAsync(M messageEntity, Callback callback);

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param messageEntity 消息实体对象
     * @param callback      推送后的回调方法
     * @param <M>           消息实体对象具体类型
     * @see MessagePushService#directlyPushAsync(String, MessageEntity, Callback)
     */
    <M extends MessageEntity> void directlyPushArrayAsync(List<M> messageEntity, Callback callback);

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param messageEntity  消息实体对象
     * @param callback       推送后的回调方法
     * @param renderStrategy 渲染策略
     * @param <M>            消息实体对象具体类型
     * @see MessagePushService#directlyPushAsync(String, MessageEntity, Callback, RenderStrategy)
     */
    <M extends MessageEntity> void directlyPushAsync(M messageEntity, Callback callback, RenderStrategy renderStrategy);

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param messageEntity  消息实体对象
     * @param callback       推送后的回调方法
     * @param renderStrategy 渲染策略
     * @param <M>            消息实体对象具体类型
     * @see MessagePushService#directlyPushAsync(String, MessageEntity, Callback, RenderStrategy)
     */
    <M extends MessageEntity> void directlyPushArrayAsync(List<M> messageEntity, Callback callback, RenderStrategy renderStrategy);

    /**
     * 推送至MQ
     *
     * @param messageEntity  消息实体对象
     * @param <M>            消息实体对象具体类型
     * @param renderStrategy 渲染策略
     * @see SystemTopic MQ消息主题
     * @see MessagePushService#pushToMQ(String, String, String, SystemTopic, MessageEntity, RenderStrategy)
     */
    <M extends MessageEntity> void pushToMQ(M messageEntity, RenderStrategy renderStrategy);

    /**
     * 推送至MQ
     *
     * @param messageEntity  消息实体对象
     * @param <M>            消息实体对象具体类型
     * @param renderStrategy 渲染策略
     * @see SystemTopic MQ消息主题
     * @see MessagePushService#pushToMQ(String, String, String, SystemTopic, MessageEntity, RenderStrategy)
     */
    <M extends MessageEntity> void pushArrayToMQ(List<M> messageEntity, RenderStrategy renderStrategy);
}
