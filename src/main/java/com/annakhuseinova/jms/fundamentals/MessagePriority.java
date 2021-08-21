package com.annakhuseinova.jms.fundamentals;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessagePriority {
    public static void main(String[] args) throws NamingException {
        InitialContext context = new InitialContext();
        Queue queue = (Queue) context.lookup("queue/myQueue");
        try(ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = connectionFactory.createContext()){
            JMSProducer producer = jmsContext.createProducer();
            String[] messages = new String[3];
            messages[0] = "Message One";
            messages[1] = "Message Two";
            messages[2] = "Message Three";

            producer.setPriority(3);
            producer.send(queue, messages[0]);

            producer.setPriority(1);
            producer.send(queue, messages[1]);

            producer.setPriority(9);
            producer.send(queue, messages[2]);

            JMSConsumer jmsConsumer= jmsContext.createConsumer(queue);

            for (int i = 0; i < 3; i++) {
                Message receivedMessage = jmsConsumer.receive();
                System.out.println(receivedMessage.getJMSPriority());
                System.out.println(jmsConsumer.receiveBody(String.class));
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
