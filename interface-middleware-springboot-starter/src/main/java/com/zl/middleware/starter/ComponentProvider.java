package com.zl.middleware.starter;

import com.zl.middleware.starter.config.SystemEnvironmentConfigProperty;
import com.zl.middleware.starter.service.SimpleMessagePushService;
import com.zl.middleware.starter.service.impl.SimpleMessagePushServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 组件配置器
 *
 * @author zl
 * @since 2021/11/23 16:05
 */
@Configuration
@EnableConfigurationProperties(SystemEnvironmentConfigProperty.class)
@ComponentScan({"com.zl.middleware.sdk", "com.zl.middleware.starter"})
public class ComponentProvider {
    /**
     * 消息推送服务
     *
     * @return 消息推送服务
     */
    @Bean
    public SimpleMessagePushService simpleMessagePushService() {
        return new SimpleMessagePushServiceImpl();
    }
}
