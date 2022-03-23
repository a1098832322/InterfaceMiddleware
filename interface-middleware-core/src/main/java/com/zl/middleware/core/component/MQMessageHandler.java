package com.zl.middleware.core.component;

/**
 * 消息队列消息管理接口
 *
 * @author 郑龙
 * @since 2019/9/16 10:28
 */
public interface MQMessageHandler {
    /**
     * 责任划分方法
     *
     * @param commandModel 命令指令
     * @param topic        MQ的topic，根据topic来自动区分接收器
     * @return true/false true表示当前对象由该Receiver接收
     */
    boolean accept(Object commandModel, String topic);

    /**
     * 抽象方法，用于后续在自定义的方法体中选择使用receiver还是sender
     * 即：方法体
     *
     * @param command 命令指令
     */
    void operate(Object command);
}
