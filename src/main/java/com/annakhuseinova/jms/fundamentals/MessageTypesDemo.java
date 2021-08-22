package com.annakhuseinova.jms.fundamentals;

import com.annakhuseinova.jms.fundamentals.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class MessageTypesDemo {

    public static void main(String[] args) throws Exception{
        InitialContext context = new InitialContext();
        Queue queue = (Queue) context.lookup("queue/myQueue");
        try(ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = connectionFactory.createContext()){
            JMSProducer producer = jmsContext.createProducer();
            TextMessage message = jmsContext.createTextMessage("Arise Awake and stop not till the goal is reached");

            BytesMessage bytesMessage = jmsContext.createBytesMessage();
            bytesMessage.writeUTF("John");
            bytesMessage.writeLong(123L);

            StreamMessage streamMessage = jmsContext.createStreamMessage();
            streamMessage.writeBoolean(true);
            streamMessage.writeFloat(2.5f);

            MapMessage mapMessage = jmsContext.createMapMessage();
            mapMessage.setBoolean("isCreditAvailable", true);

            Patient patient = new Patient();
            patient.setId(123);
            patient.setName("John");
            ObjectMessage objectMessage = jmsContext.createObjectMessage();
            objectMessage.setObject(patient);
            producer.send(queue, objectMessage);
            ObjectMessage messageReceived = (ObjectMessage) jmsContext.createConsumer(queue).receive(5000);
            Patient patientReceived =  (Patient) messageReceived.getObject();
            System.out.println(patientReceived);

        }
    }
}
