package com.zl.middleware.core.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.TextMessage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

/**
 * 消息中间件MQ消息接收器
 *
 * @author zl
 * @since 2021/11/22 17:28
 */
@Slf4j
public class MessageMiddlewareMQReceiver extends AbstractMQManager {

    /**
     * 注入MQ会话管理器
     */
    @Autowired
    private DirectSingleMessageSender directMessageSender;

    /**
     * 处理消息的方法
     *
     * @param msg 消息对象
     */
    @Override
    public void handleMessage(Object msg) {
        if (msg instanceof TextMessage) {
            try {
                //还原消息对象
                Map<String, Object> params = Optional.ofNullable(((TextMessage) msg).getText())
                        .map(s -> {
                            log.info("Received data : {}", s);
                            return JSON.parseObject(s, Map.class);
                        })
                        .map(map -> (Map<String, Object>) map)
                        .map(stringObjectMap -> {
                            stringObjectMap.forEach((key, value) -> {
                                //解决一些类型转换问题
                                if (key.equals("interfaceKeys") && value instanceof JSONArray) {
                                    stringObjectMap.put(key, JSON.parseObject(JSON.toJSONString(value), ArrayList.class));
                                }

                                //double被序列化为了BigDecimal
                                if (value instanceof BigDecimal) {
                                    stringObjectMap.put(key, ((BigDecimal) value).doubleValue());
                                }
                            });
                            return stringObjectMap;
                        }).orElse(null);

                //直接推送
                directMessageSender.multithreadingSendMessage(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 消息发送方法
     *
     * @param destinationName 消息队列名称
     * @param messageObject   消息体对象
     */
    @Override
    public final void sendMessage(String destinationName, Object messageObject) {
        //这个是消息接收器，默认不实现消息发送机制
    }

    /**
     * 责任划分方法
     *
     * @param commandModel 命令指令
     * @param topic        MQTT的topic，根据topic来自动区分接收器
     * @return true/false true表示当前对象由该Receiver接收
     */
    @Override
    public boolean accept(Object commandModel, String topic) {
        //接收所有系统主题推送的消息
        return true;
    }
}
