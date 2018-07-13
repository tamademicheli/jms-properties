package sandbox.jmsproperties;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.jms.*;
import java.util.UUID;

/**
 * Created by tama on 01/06/2018.
 */
@RestController()
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

    @Autowired
    SelectorHolder holder;

    @RequestMapping("/add/consumer1/{contractor}")
    public String addConsumer1(@PathVariable("contractor") String contractor) throws Exception {
        holder.addSelectionConsumer1(contractor);
        return "added contractor to Dynamic Consumer 1: "+ contractor;

    }

    @RequestMapping("/reset")
    public void reset() throws Exception {
        context.getAutowireCapableBeanFactory().createBean(DynamicConsumer1.class);
    }

    @RequestMapping("/send/{contractor}")
    public String index(@PathVariable("contractor") String contractor) throws Exception {


        variant2(contractor);
        //variant1();


        return "Greetings from Spring Boot!";
    }

    private void variant2(@PathVariable("contractor") String contractor) {
        jmsTemplate.convertAndSend(queueName, "POOOOOOORRCOOOOOOODDDIIIOOOOO"+contractor, m -> {

            m.setJMSDestination(new ActiveMQQueue(queueName));
            m.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
            m.setJMSPriority(Message.DEFAULT_PRIORITY);
            m.setJMSTimestamp(System.nanoTime());
            m.setJMSType(contractor);
            m.setJMSExpiration(500);

            m.setStringProperty("jms-custom-header", "this is a custom jms property");
            m.setBooleanProperty("jms-custom-property", true);
            m.setDoubleProperty("jms-custom-property-price", 0.0);
            return m;
        });
    }

    private void variant1() throws JMSException {
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
    }

}
