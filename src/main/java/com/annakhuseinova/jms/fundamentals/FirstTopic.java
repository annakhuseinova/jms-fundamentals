 package com.annakhuseinova.jms.fundamentals;

import javax.jms.*;
import javax.naming.InitialContext;

public class FirstTopic {

    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Topic topic = (Topic) initialContext.lookup("topic.topic/myTopic");
        ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession();
        MessageProducer producer = session.createProducer(topic);
        MessageConsumer firstConsumer = session.createConsumer(topic);
        MessageConsumer secondConsumer = session.createConsumer(topic);
        TextMessage message = session.createTextMessage("All the power is within me. I can do anything and everything");
        producer.send(message);
        connection.start();
        TextMessage message1 = (TextMessage) firstConsumer.receive(5000);
        TextMessage message2 = (TextMessage) secondConsumer.receive(5000);
        System.out.println(message1 + " " + message2);
        connection.close();
        initialContext.close();
    }
}








