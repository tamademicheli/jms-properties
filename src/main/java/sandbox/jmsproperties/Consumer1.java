package sandbox.jmsproperties;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer1 {


    @JmsListener(destination = "${jms.properties.example}", selector =  "${jms.type1}" )
    public void receive(String message) {
        System.out.println("received message from consumer1");
    }
}
