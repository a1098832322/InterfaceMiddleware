package com.zl.middleware.core.config;

import com.zl.middleware.core.component.AbstractMQManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import javax.jms.*;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Active MQ  会话管理器
 *
 * @author 郑龙
 * @since 2019/9/16 9:28
 * @since 1.1.6
 */
@Data
@Slf4j
@Configurable(autowire = Autowire.BY_TYPE, dependencyCheck = true)
public class ActiveMQWrapper implements MessageListener {
    @Autowired
    private MQProperties mqProperties;

    @Autowired
    @Qualifier("activeMqConnection")
    private Connection activeMqConnection;

    @Autowired
    @Qualifier("mqttConnectionFactory")
    private ActiveMQConnectionFactory connectionFactory;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 消费者map
     */
    private Map<String, MessageConsumer> consumerMap = new ConcurrentHashMap<>(16);

    /**
     * 生产者map
     */
    private Map<String, MessageProducer> producerMap = new ConcurrentHashMap<>(16);

    /**
     * 根据订阅的主题名称得到消息生产者对象
     *
     * @param destinationName 订阅的主题名称
     * @return MessageProducer
     */
    public MessageProducer getProducer(String destinationName) {
        Objects.requireNonNull(destinationName, "订阅的主题名称不能为空！");
        return producerMap.get(destinationName);
    }

    @PostConstruct
    public void init() throws JMSException {
        if (StringUtils.isNotBlank(mqProperties.getQueueConsumer().getNames())) {
            String[] themes = mqProperties.getQueueConsumer().getNames().split(",");
            for (String theme : themes) {
                Session session = createActiveMqSession();
                Destination destination = session.createQueue(theme);
                MessageConsumer consumer = session.createConsumer(destination);
                consumer.setMessageListener(this);
                //加入map
                this.getConsumerMap().put(theme, consumer);

                //console debug
                log.info("Listening theme: {} as queue consumer.", theme);
            }
        }

        if (StringUtils.isNotBlank(mqProperties.getQueueProducer().getNames())) {
            String[] themes = mqProperties.getQueueProducer().getNames().split(",");
            for (String theme : themes) {
                Session session = createActiveMqSession();
                Destination destination = session.createQueue(theme);
                MessageProducer producer = session.createProducer(destination);
                //加入map
                this.getProducerMap().put(theme, producer);

                //console debug
                log.info("Listening theme: {} as queue producer.", theme);
            }
        }

        if (StringUtils.isNotBlank(mqProperties.getTopicConsumer().getNames())) {
            String[] themes = mqProperties.getTopicConsumer().getNames().split(",");
            for (String theme : themes) {
                Session session = createActiveMqSession();
                Destination destination = session.createTopic(theme);
                MessageConsumer consumer = session.createConsumer(destination);
                consumer.setMessageListener(this);
                //加入map
                this.getConsumerMap().put(theme, consumer);

                //console debug
                log.info("Listening theme: {} as topic consumer.", theme);
            }
        }

        if (StringUtils.isNotBlank(mqProperties.getTopicProducer().getNames())) {
            String[] themes = mqProperties.getTopicProducer().getNames().split(",");
            for (String theme : themes) {
                Session session = createActiveMqSession();
                Destination destination = session.createTopic(theme);
                MessageProducer producer = session.createProducer(destination);
                //加入map
                this.getProducerMap().put(theme, producer);

                //console debug
                log.info("Listening theme: {} as topic producer.", theme);
            }
        }

    }

    /**
     * 创建Active MQ消息session
     *
     * @return active mq 消息session
     * @throws JMSException jms异常
     */
    private Session createActiveMqSession() throws JMSException {
        //使用自动确认模式创建session
        return activeMqConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    /**
     * 根据订阅的主题名称得到消息消费者对象
     *
     * @param destinationName 订阅的主题名称
     * @return MessageConsumer
     */
    public MessageConsumer getConsumer(String destinationName) {
        Objects.requireNonNull(destinationName, "订阅的主题名称不能为空！");
        return consumerMap.get(destinationName);
    }

    /**
     * Passes a message to the listener.
     *
     * @param message the message passed to the listener
     */
    @Override
    public void onMessage(Message message) {
        String destinationName = ((ActiveMQMessage) message).getDestination().getPhysicalName();
        applicationContext.getBeansOfType(AbstractMQManager.class).values().forEach(abstractMQManager -> {
            if (abstractMQManager.accept(message, destinationName)) {
                abstractMQManager.operate(message);
            }
        });
    }

    /**
     * 发送文本消息
     *
     * @param themeName   主题名
     * @param textMessage 文本消息
     */
    public void sendTextMessage(String themeName, String textMessage) {
        if (textMessage == null) {
            textMessage = "";
        }

        try {
            ActiveMQMessageProducer producer = (ActiveMQMessageProducer) getProducer(themeName);
            TextMessage msg = new ActiveMQTextMessage();
            msg.setText(textMessage);
            producer.send(msg);
        } catch (JMSException e) {
            onExceptionCaught(e);
        }
    }

    /**
     * 自动捕获异常
     *
     * @param e 异常
     */
    public void onExceptionCaught(Exception e) {
        log.error(this.getClass().getName() + "捕获到异常: ", e);
    }
}
