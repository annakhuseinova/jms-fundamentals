package com.annakhuseinova.jms.fundamentals;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;
import java.util.Objects;

public class QueueBrowserDemo {
    public static void main(String[] args) {
        InitialContext initialContext = null;
        Connection connection = null;
        try {
            // By default, initialContext will use jndi.properties file to get the properties
            initialContext = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            Queue queue = (Queue) initialContext.lookup("queue/myQueue");
            MessageProducer producer = session.createProducer(queue);
            TextMessage firstMessage = session.createTextMessage("Message 1");
            TextMessage secondMessage = session.createTextMessage("Message 2");
            producer.send(firstMessage);
            producer.send(secondMessage);
            QueueBrowser queueBrowser = session.createBrowser(queue);

            Enumeration enumeration = queueBrowser.getEnumeration();

            while (enumeration.hasMoreElements()){
                TextMessage message = (TextMessage) enumeration.nextElement();
                System.out.println("Browsing : " + message.getText());
            }
            MessageConsumer consumer = session.createConsumer(queue);
            // connection.start() is not needed when we send a message to a queue, but we need it
            // to receive a message. connection.start() tells the JMS provider that the consumer is ready to receive
            // messages
            connection.start();
            TextMessage message = (TextMessage) consumer.receive(5000);
            message = (TextMessage) consumer.receive(5000);
            System.out.println("Message received: " + message.getText());
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(initialContext)){
                try {
                    initialContext.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
            if (Objects.nonNull(connection)){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
