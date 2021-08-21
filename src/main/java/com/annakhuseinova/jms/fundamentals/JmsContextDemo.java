package com.annakhuseinova.jms.fundamentals;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;

/**
 * Example of JMS 2.0 use
 * */
public class JmsContextDemo {

    public static void main(String[] args) throws Exception {
        InitialContext context = new InitialContext();
        Queue queue = (Queue) context.lookup("queue/myQueue");

        try(ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = connectionFactory.createContext()){
            jmsContext.createProducer().send(queue, "Arise Awake and stop not till goal is reached");
            jmsContext.createConsumer(queue).receiveBody(String.class);
            String messageReceived = jmsContext.createConsumer(queue).receiveBody(String.class);
            System.out.println(messageReceived);
        }
    }
}
