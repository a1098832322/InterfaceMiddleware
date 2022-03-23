package com.zl.middleware.core.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * ActiveMQ配置
 *
 * @author zl
 * @version 1.0.0
 * @since 2021年11月23日
 */
@Slf4j
@Order(2)
@Configuration
@EnableConfigurationProperties({ActiveMQProperties.class, MQProperties.class})
public class ActiveMQConfig {

    /**
     * 创建Active MQ连接bean
     *
     * @param properties 属性参数
     * @return bean (name = mqttConnectionFactory)
     */
    @Bean("mqttConnectionFactory")
    @ConditionalOnProperty(prefix = "spring.activemq.packages", value = {"trust-all"},
            havingValue = "true")
    public ActiveMQConnectionFactory connectionFactory(ActiveMQProperties properties) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(properties.getBrokerUrl());
        connectionFactory.setUserName(properties.getUser());
        connectionFactory.setPassword(properties.getPassword());
        connectionFactory.setTrustAllPackages(true);
        return connectionFactory;

    }

    /**
     * 消息session
     *
     * @param connectionFactory active mq 消息session
     * @return active mq 消息session
     * @throws JMSException jms异常
     */
    @Bean("activeMqConnection")
    @ConditionalOnBean(ActiveMQConnectionFactory.class)
    public Connection activeMqConnection(@Qualifier("mqttConnectionFactory") ActiveMQConnectionFactory connectionFactory)
            throws JMSException {
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
        } catch (JMSException jmse) {
            if (connection != null) {
                connection.close();
            }
            throw jmse;
        }

        //返回MQ连接
        return connection;
    }

    /**
     * Active MQ管理中心
     *
     * @return Active MQ管理中心实例
     */
    @Bean
    @ConditionalOnBean(Connection.class)
    public ActiveMQWrapper activeMQWrapper() {
        return new ActiveMQWrapper();
    }


}
