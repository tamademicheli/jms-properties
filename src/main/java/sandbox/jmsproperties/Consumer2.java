package sandbox.jmsproperties;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class Consumer2 {


    @JmsListener(destination = "${jms.properties.example}", selector =  "${jms.type2}" )
    public void receive(String message) {
        // @Header(JmsHeaders.CORRELATION_ID) String correlationId,
        System.out.println("received message from consumer2");

    }
}
