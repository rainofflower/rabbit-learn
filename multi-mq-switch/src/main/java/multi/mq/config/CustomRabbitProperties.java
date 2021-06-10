package multi.mq.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;

import java.util.List;

/**
 * rabbit配置
 *
 **/
@Data
public class CustomRabbitProperties extends RabbitProperties {

    private List<Binding> bindings;

    private List<Queue> queues;
}
