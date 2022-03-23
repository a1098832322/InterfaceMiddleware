package com.zl.middleware.sdk.service;

import com.zl.middleware.sdk.def.MessageEntity;
import com.zl.middleware.sdk.def.RenderStrategy;
import com.zl.middleware.sdk.def.SystemTopic;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 消息推送服务
 *
 * @author zl
 * @since 2021/11/19 9:48
 */
public interface MessagePushService {
    /**
     * 设置HTTP消息头
     *
     * @param headers HTTP消息头
     */
    MessagePushService setHttpHeaders(Map<String, String> headers);

    /**
     * 设置请求的目标接口key
     *
     * @param keys 请求的目标接口key
     * @return MessagePushService.this
     */
    MessagePushService setTargetInterfaceKeys(String... keys);

    /**
     * 设置请求的目标系统模块key
     *
     * @param keys 目标系统模块key
     * @return MessagePushService.this
     */
    MessagePushService setTargetSystemKeys(String... keys);

    /**
     * 直接推送到中间件的消息直推接口
     *
     * @param url           中间件消息直推接口的完整URL
     * @param messageEntity 消息实体对象
     * @param <M>           消息实体对象具体类型
     */
    <M extends MessageEntity> Response directlyPush(String url, M messageEntity) throws IOException;

    /**
     * 直接推送到中间件的消息直推接口(推送JSON Array)
     *
     * @param url           中间件消息直推接口的完整URL
     * @param messageEntity 消息实体对象
     * @param <M>           消息实体对象具体类型
     */
    <M extends MessageEntity> Response directlyPushArray(String url, List<M> messageEntity) throws IOException;

    /**
     * 直接推送到中间件的消息直推接口
     *
     * @param url            中间件消息直推接口的完整URL
     * @param messageEntity  消息实体对象
     * @param renderStrategy 渲染策略
     * @param <M>            消息实体对象具体类型
     */
    <M extends MessageEntity> Response directlyPush(String url, M messageEntity, RenderStrategy renderStrategy) throws IOException;

    /**
     * 直接推送到中间件的消息直推接口
     *
     * @param url            中间件消息直推接口的完整URL
     * @param messageEntity  消息实体对象
     * @param renderStrategy 渲染策略
     * @param <M>            消息实体对象具体类型
     */
    <M extends MessageEntity> Response directlyPushArray(String url, List<M> messageEntity, RenderStrategy renderStrategy) throws IOException;

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param url           中间件消息直推接口的完整URL
     * @param messageEntity 消息实体对象
     * @param callback      推送后的回调方法
     * @param <M>           消息实体对象具体类型
     */
    <M extends MessageEntity> void directlyPushAsync(String url, M messageEntity, Callback callback);

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param url           中间件消息直推接口的完整URL
     * @param messageEntity 消息实体对象
     * @param callback      推送后的回调方法
     * @param <M>           消息实体对象具体类型
     */
    <M extends MessageEntity> void directlyPushArrayAsync(String url, List<M> messageEntity, Callback callback);

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param url            中间件消息直推接口的完整URL
     * @param messageEntity  消息实体对象
     * @param callback       推送后的回调方法
     * @param renderStrategy 渲染策略
     * @param <M>            消息实体对象具体类型
     */
    <M extends MessageEntity> void directlyPushAsync(String url, M messageEntity, Callback callback, RenderStrategy renderStrategy);

    /**
     * (异步)直接推送到中间件的消息直推接口
     *
     * @param url            中间件消息直推接口的完整URL
     * @param messageEntity  消息实体对象
     * @param callback       推送后的回调方法
     * @param renderStrategy 渲染策略
     * @param <M>            消息实体对象具体类型
     */
    <M extends MessageEntity> void directlyPushArrayAsync(String url, List<M> messageEntity, Callback callback, RenderStrategy renderStrategy);

    /**
     * 推送至MQ
     *
     * @param mqUser         登录MQ的账号
     * @param mqPassword     登录MQ的密码
     * @param mqBrokerUrl    MQ服务的URL地址，例如：tcp://192.168.200.2:61616
     * @param topic          MQ消息主题
     * @param messageEntity  消息实体对象
     * @param <M>            消息实体对象具体类型
     * @param renderStrategy 渲染策略
     * @see SystemTopic MQ消息主题
     */
    <M extends MessageEntity> void pushToMQ(String mqUser, String mqPassword, String mqBrokerUrl, SystemTopic topic, M messageEntity, RenderStrategy renderStrategy);

    /**
     * 推送至MQ
     *
     * @param mqUser         登录MQ的账号
     * @param mqPassword     登录MQ的密码
     * @param mqBrokerUrl    MQ服务的URL地址，例如：tcp://192.168.200.2:61616
     * @param topic          MQ消息主题
     * @param messageEntity  消息实体对象
     * @param <M>            消息实体对象具体类型
     * @param renderStrategy 渲染策略
     * @see SystemTopic MQ消息主题
     */
    <M extends MessageEntity> void pushArrayToMQ(String mqUser, String mqPassword, String mqBrokerUrl, SystemTopic topic, List<M> messageEntity, RenderStrategy renderStrategy);
}
