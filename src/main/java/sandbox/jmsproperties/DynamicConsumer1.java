package sandbox.jmsproperties;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component("dynamicConsumer1")
public class DynamicConsumer1 {
    @JmsListener(destination = "${jms.properties.example}", selector =  "#{selectorHolder.buildSelectorConsumer1()}" )
    public void receive(String message, @Header(JmsHeaders.TYPE) String jmsType) {

        System.out.println("received message from Dynamic consumer1:"+message);
        System.out.println("received header from Dynamic consumer1:"+jmsType);
        System.out.println("instance Dynamic consumer1:"+this.toString());
    }
}
