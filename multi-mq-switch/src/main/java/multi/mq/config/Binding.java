package multi.mq.config;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class Binding  implements Serializable {

    private static final long serialVersionUID = -1;

    private  String destination;
    private  String exchange;
    private  String routingKey;
    private  Map<String, Object> arguments;
    private  String destinationType;

    public boolean isDestinationQueue() {
        return org.springframework.amqp.core.Binding.DestinationType.QUEUE.name().equals(this.destinationType);
    }
}
