package com.annakhuseinova.jms.fundamentals;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class MessagePropertiesDemo {

    public static void main(String[] args) throws Exception{
        InitialContext context = new InitialContext();
        Queue queue = (Queue) context.lookup("queue/myQueue");
        try(ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = connectionFactory.createContext()){
            JMSProducer producer = jmsContext.createProducer();
            TextMessage message = jmsContext.createTextMessage("Arise Awake and stop not till the goal is reached");
            message.setBooleanProperty("loggedIn", true);
            message.setStringProperty("userToken", "abc123");

            producer.send(queue, message);
            StreamMessage messageReceived = (StreamMessage) jmsContext.createConsumer(queue).receive(5000);
            System.out.println(messageReceived.readBoolean());
            System.out.println(messageReceived.readFloat());
        }
    }
}
