package sandbox.jmsproperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;

/**
 * Created by tama on 01/06/2018.
 */
@RestController()
@RequestMapping("/jms")
public class MessageSender {

    @Autowired
    private ApplicationContext context;


    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    Destination propertiesQueueDestination;

    @Value("${jms.properties.example}")
    String queueName;


    @Autowired
    ConnectionFactory connectionFactory;


    @RequestMapping("/send")
    public String index() throws Exception {

        // Create a Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Create a Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);


        // Create a MessageProducer from the Session to the Topic or Queue
        MessageProducer producer = session.createProducer(propertiesQueueDestination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        // Create a messages
        String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
        TextMessage message = session.createTextMessage(text);
        message.setStringProperty("original.queue", queueName);


        // Tell the producer to send the message
        System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
        producer.send(message);

        // Clean up
        session.close();
        connection.close();


        //      jmsTemplate.setConnectionFactory(connectionFactory);
        //      jmsMessagingTemplate.setConnectionFactory(connectionFactory);
        //  ActiveMQTextMessage message = new ActiveMQTextMessage();
        // MessageBuilder
        // jmsMessagingTemplate.send(propertiesQueueDestination,message);


        return "Greetings from Spring Boot!";
    }

}
