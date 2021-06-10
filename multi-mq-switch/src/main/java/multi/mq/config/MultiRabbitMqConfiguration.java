package multi.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.SimpleResourceHolder;
import org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 多rabbitmq装配
 */
@Configuration
@EnableConfigurationProperties(MultiRabbitMqProperties.class)
@EnableRabbit
@Slf4j
public class MultiRabbitMqConfiguration {

    private AtomicBoolean initialized = new AtomicBoolean(false);

    @Autowired
    private MultiRabbitMqProperties properties;

    /**
     * 连接工厂
     * @return
     */
    @Bean
    @Primary
    public ConnectionFactory rabbitConnectionFactory(){
        SimpleRoutingConnectionFactory routingConnectionFactory = new SimpleRoutingConnectionFactory();
        Map<String, CustomRabbitProperties> propertiesMqMap = properties.getMqMap();
        Map<Object, ConnectionFactory> connectionFactoryMap = new HashMap<>();
        for(Map.Entry<String, CustomRabbitProperties> item : propertiesMqMap.entrySet()){
            CustomRabbitProperties properties = item.getValue();
            CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
            connectionFactory.setHost(properties.getHost());
            connectionFactory.setPort(properties.getPort());
            connectionFactory.setVirtualHost(properties.getVirtualHost());
            connectionFactory.setUsername(properties.getUsername());
            connectionFactory.setPassword(properties.getPassword());
            connectionFactoryMap.put(item.getKey(), connectionFactory);
        }
        log.info("共加载{}个rabbitmq工厂",connectionFactoryMap.size());
        routingConnectionFactory.setTargetConnectionFactories(connectionFactoryMap);
        routingConnectionFactory.setDefaultTargetConnectionFactory(connectionFactoryMap.get(properties.getDefaultMq()));
        return routingConnectionFactory;
    }

    @Bean
    public CustomSimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        CustomSimpleRabbitListenerContainerFactory factory = new CustomSimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        this.init(rabbitTemplate);
        return rabbitTemplate;
    }

    /**
     * 初始化queue、binding、exchange
     * @param rabbitTemplate
     */
    private void init(RabbitTemplate rabbitTemplate){
        if(initialized.compareAndSet(false, true)){
            Map<String, CustomRabbitProperties> propertiesMqMap = properties.getMqMap();
            for(Map.Entry<String, CustomRabbitProperties> item : propertiesMqMap.entrySet()) {
                String name = item.getKey();
                SimpleResourceHolder.bind(rabbitTemplate.getConnectionFactory(), name);
                CustomRabbitProperties properties = item.getValue();
                final List<Queue> queues = properties.getQueues();
                final List<Binding> bindings = properties.getBindings();
                rabbitTemplate.execute(channel -> {
                    for(Queue queue : queues){
                        channel.queueDeclare(queue.getName(), queue.isDurable(),
                                queue.isExclusive(), queue.isAutoDelete(), queue.getArguments());
                    }
                    for(Binding binding : bindings){
                        if (binding.isDestinationQueue()) {
                            channel.queueBind(binding.getDestination(), binding.getExchange(), binding.getRoutingKey(), binding.getArguments());
                        }
                        else {
                            channel.exchangeBind(binding.getDestination(), binding.getExchange(), binding.getRoutingKey(),
                                    binding.getArguments());
                        }
                    }
                    return null;
                });
                SimpleResourceHolder.unbind(rabbitTemplate.getConnectionFactory());
            }
        }
    }
}