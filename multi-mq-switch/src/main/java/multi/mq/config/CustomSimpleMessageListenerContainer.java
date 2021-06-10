package multi.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.SimpleResourceHolder;
import org.springframework.amqp.rabbit.listener.BlockingQueueConsumer;
import org.springframework.amqp.rabbit.listener.MethodRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

/**
 * 多mq ListenerContainer
 *
 **/
@Slf4j
public class CustomSimpleMessageListenerContainer extends SimpleMessageListenerContainer {

    private RabbitListenerEndpoint endpoint;

    public RabbitListenerEndpoint getEndpoint(){
        return this.endpoint;
    }

    public void setEndpoint(RabbitListenerEndpoint endpoint){
        this.endpoint = endpoint;
    }

    protected String getRoutingLookupKey() {
        Object key = SimpleResourceHolder.get(this.getRoutingConnectionFactory());
        return key == null ? null : key.toString();
    }

    protected BlockingQueueConsumer createBlockingQueueConsumer() {
        final MethodRabbitListenerEndpoint methodEndpoint = (MethodRabbitListenerEndpoint)endpoint;
        TargetMq targetMq = methodEndpoint.getMethod().getDeclaredAnnotation(TargetMq.class);
        String value = targetMq.value();
        SimpleResourceHolder.bind(getRoutingConnectionFactory(), value);
        BlockingQueueConsumer consumer = super.createBlockingQueueConsumer();
        SimpleResourceHolder.unbind(getRoutingConnectionFactory());
        return consumer;
    }
}
