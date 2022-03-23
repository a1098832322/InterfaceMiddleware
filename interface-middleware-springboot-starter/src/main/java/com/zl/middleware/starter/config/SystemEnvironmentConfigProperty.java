package com.zl.middleware.starter.config;

import com.zl.middleware.sdk.def.SystemTopic;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统环境配置
 *
 * @author zl
 * @since 2021/11/23 16:17
 */
@Data
@ConfigurationProperties(prefix = "middleware.message")
public class SystemEnvironmentConfigProperty {
    /**
     * 推送服务
     */
    private Push push;

    /**
     * MQ服务
     */
    private MQ mq;

    /**
     * 消息推送服务对象
     */
    @Data
    public static class Push {
        /**
         * 消息推送服务主机地址<br>
         * 如：http://127.0.0.1:8080
         */
        private String servicesHost;

        /**
         * 消息推送服务的URI接口地址<br>
         * 如：/message/push
         */
        private String uri;
    }

    /**
     * MQ配置
     */
    @Data
    public static class MQ {
        /**
         * 与MQ服务器建立TCP连接的
         */
        private String tcpUrl;

        /**
         * MQ登录账号
         */
        private String user;

        /**
         * MQ登录密码
         */
        private String password;

        /**
         * 当前系统发送的消息主题
         */
        private SystemTopic topic;
    }

}
