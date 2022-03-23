package com.zl.middleware.core.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置ActiveMQ的各种主题
 *
 * @author 郑龙
 * @since 2019/9/12 17:48
 */
@Data
@ConfigurationProperties(prefix = "spring.activemq.theme")
public class MQProperties {
    /**
     * 消费者实例
     */
    private QueueConsumer queueConsumer = new QueueConsumer();

    /**
     * topic模式消费者实例
     */
    private TopicConsumer topicConsumer = new TopicConsumer();

    /**
     * topic模式生产者实例
     */
    private TopicProducer topicProducer = new TopicProducer();

    /**
     * 生产者实例
     */
    private QueueProducer queueProducer = new QueueProducer();

    /**
     * 生产者相关配置
     */
    @Data
    @ConditionalOnProperty(prefix = "queue.consumer")
    public static class QueueConsumer {
        /**
         * 消费者订阅主题名
         */
        private String names;
    }

    @Data
    @ConditionalOnProperty(prefix = "queue.producer")
    public static class QueueProducer {
        /**
         * 生产者订阅主题名
         */
        private String names;
    }

    @Data
    @ConditionalOnProperty(prefix = "topic.consumer")
    public static class TopicConsumer {
        /**
         * topic模式消费者订阅主题名
         */
        private String names;
    }

    @Data
    @ConditionalOnProperty(prefix = "topic.producer")
    public static class TopicProducer {
        /**
         * topic模式生产者订阅主题名
         */
        private String names;
    }
}
