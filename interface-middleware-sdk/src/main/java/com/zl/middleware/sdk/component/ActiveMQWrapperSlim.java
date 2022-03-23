package com.zl.middleware.sdk.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.commons.lang3.StringUtils;

import javax.jms.*;

/**
 * 适用于ActiveMQ的消息发送组件(青春版)
 *
 * @author zl
 * @since 2021/11/23 9:49
 */
@Slf4j
public class ActiveMQWrapperSlim {

    private volatile static ActiveMQWrapperSlim instance;

    private final ConnectionFactory connectionFactory;

    private ActiveMQWrapperSlim(String mqBrokerUrl, String user, String password) {
        this.connectionFactory = new ActiveMQConnectionFactory(StringUtils.isEmpty(user) ? ActiveMQConnectionFactory.DEFAULT_USER : user,
                StringUtils.isEmpty(password) ? ActiveMQConnectionFactory.DEFAULT_PASSWORD : password, mqBrokerUrl);
    }

    /**
     * 双锁单例
     *
     * @param mqBrokerUrl MQ服务的URL地址，例如：tcp://192.168.200.2:61616
     * @return ActiveMQWrapperSlim instance
     */
    public static ActiveMQWrapperSlim getInstance(String mqBrokerUrl, String user, String password) {
        if (instance == null) {
            synchronized (ActiveMQWrapperSlim.class) {
                if (instance == null) {
                    instance = new ActiveMQWrapperSlim(mqBrokerUrl, user, password);
                }
            }

        }

        return instance;
    }

    /**
     * 向指定的topic发送消息
     *
     * @param topic   指定的topic,通常来自于{@link com.zl.middleware.sdk.def.SystemTopic}中的定义
     * @param message 文本消息
     */
    public void sendTextMessage(String topic, String message) throws JMSException {
        //建立连接
        Connection connection = connectionFactory.createConnection();
        connection.start();
        //开启事务
        Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
        Destination destination = session.createTopic(topic);
        //创建消息发送器
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        //设置消息文本
        TextMessage textMessage = new ActiveMQTextMessage();
        textMessage.setText(message);
        //执行发送
        producer.send(textMessage);
        //关闭连接
        session.close();
        connection.close();
    }

}
