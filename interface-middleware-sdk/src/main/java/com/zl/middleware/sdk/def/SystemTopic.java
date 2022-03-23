package com.zl.middleware.sdk.def;

import lombok.Getter;

/**
 * 消息主题
 *
 * @author zl
 * @since 2021/11/23 9:40
 */
public enum SystemTopic {
    /**
     * ims系统
     */
    IMS("IMS_MESSAGE"),

    /**
     * dms系统
     */
    DMS("DMS_MESSAGE"),

    /**
     * osp系统
     */
    OSP("OSP_MESSAGE"),

    /***
     * 其它系统
     */
    OTHER("OTHER_SYSTEM_MESSAGE");

    @Getter
    final String topic;

    SystemTopic(String topic) {
        this.topic = topic;
    }
}
