package com.annakhuseinova.jms.fundamentals;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class RequestReplyDemo {

    public static void main(String[] args) throws NamingException {
        InitialContext context = new InitialContext();
        Queue requestQueue = (Queue) context.lookup("queue/requestQueue");
        Queue replyQueue = (Queue) context.lookup("queue/replyQueue");
        try(ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = connectionFactory.createContext()){
            JMSProducer producer = jmsContext.createProducer();
            TextMessage message = jmsContext.createTextMessage("Arise Awake and stop not till the goal is reached");
            message.setJMSReplyTo(replyQueue);
            producer.send(requestQueue, message);
            JMSConsumer requestConsumer = jmsContext.createConsumer(requestQueue);
            TextMessage messageReceived = (TextMessage) requestConsumer.receive();
            System.out.println(messageReceived.getText());

            JMSProducer replyProducer = jmsContext.createProducer();
            replyProducer.send(messageReceived.getJMSReplyTo(), "You are awesome!");

            JMSConsumer replyConsumer = jmsContext.createConsumer(messageReceived.getJMSReplyTo());
            System.out.println(replyConsumer.receiveBody(String.class));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
