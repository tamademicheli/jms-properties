package sandbox.jmsproperties;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Component("selectorHolder")
public class SelectorHolder {

    Set<String> selectorConsumer1 = new HashSet<>();
    Set<String> selectorConsumer2 = new HashSet<>();


    @PostConstruct
    public void postConstuct(){
        IntStream.range(0, 1000).mapToObj(String::valueOf).forEach(selectorConsumer1::add);
    }

    public synchronized  void addSelectionConsumer1(String contractor){
        System.out.println("added to selector Dynamic Consumer1: "+contractor);
        selectorConsumer1.add(contractor);
    }

    public synchronized  void addSelectionConsumer2(String contractor){
        selectorConsumer2.add(contractor);
    }

    public String buildSelectorConsumer1(){
        StringBuilder sb = new StringBuilder();
        selectorConsumer1.stream().forEach(entry1 -> {
            sb.append("JMSType='").append(entry1).append("' OR ");
        });
        System.out.println("Selector Dynamic Consumer 1: "+sb.toString());
        if (sb.length() > 3){
            sb.replace(sb.toString().length()- 3, sb.toString().length(),"");
            return sb.toString();
        }

        return "JMSType='NONE'";


    }
}
