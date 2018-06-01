package sandbox.jmsproperties;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;

@Configuration
public class ActiveMqConfiguration {

    public static final String ADDRESS = "tcp://localhost:61616";


    @Bean(name = "propertiesQueueDestination")
    public Destination propertiesQueueDestination(@Value("${jms.properties.example}") String propertiesQueueName)
            throws JMSException {
        return new ActiveMQQueue(propertiesQueueName);
    }


    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(ADDRESS);
    }
}