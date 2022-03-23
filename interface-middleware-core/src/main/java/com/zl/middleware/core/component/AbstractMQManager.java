package com.zl.middleware.core.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * ActiveMQ接口
 *
 * @author 郑龙
 * @since  2019/8/14 18:49
 */
@Slf4j
@Configurable
public abstract class AbstractMQManager implements MQMessageHandler {
    /**
     * 处理消息的方法
     *
     * @param msg 消息对象
     */
    public abstract void handleMessage(Object msg);

    /**
     * 消息发送方法
     *
     * @param destinationName 消息队列名称
     * @param messageObject   消息体对象
     */
    public abstract void sendMessage(String destinationName, Object messageObject);

    /**
     * 当异常产生时调用的方法
     *
     * @param e 异常
     */
    public void onExceptionCaught(Exception e) {
        log.error(this.getClass().getName() + "捕获到异常： ", e);
    }

    /**
     * 使用自定义方法处理
     *
     * @param command 命令指令
     */
    @Override
    public void operate(Object command) {
        try {
            handleMessage(command);
        } catch (Exception e) {
            onExceptionCaught(e);
        }
    }
}
