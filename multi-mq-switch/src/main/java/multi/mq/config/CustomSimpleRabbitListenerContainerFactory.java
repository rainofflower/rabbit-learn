package multi.mq.config;

import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpoint;

/**
 * 多mq ListenerContainerFactory
 * 初始RabbitListenerEndpoint信息
 *
 **/
public class CustomSimpleRabbitListenerContainerFactory
        extends AbstractRabbitListenerContainerFactory<CustomSimpleMessageListenerContainer> {

    private Integer txSize;

    private Integer concurrentConsumers;

    private Integer maxConcurrentConsumers;

    private Long startConsumerMinInterval;

    private Long stopConsumerMinInterval;

    private Integer consecutiveActiveTrigger;

    private Integer consecutiveIdleTrigger;

    private Long receiveTimeout;

    private Boolean deBatchingEnabled;

    public void setTxSize(Integer txSize) {
        this.txSize = txSize;
    }

    public void setConcurrentConsumers(Integer concurrency) {
        this.concurrentConsumers = concurrency;
    }


    public void setMaxConcurrentConsumers(Integer maxConcurrency) {
        this.maxConcurrentConsumers = maxConcurrency;
    }


    public void setStartConsumerMinInterval(Long minStartInterval) {
        this.startConsumerMinInterval = minStartInterval;
    }


    public void setStopConsumerMinInterval(Long minStopInterval) {
        this.stopConsumerMinInterval = minStopInterval;
    }

    public void setConsecutiveActiveTrigger(Integer minConsecutiveActive) {
        this.consecutiveActiveTrigger = minConsecutiveActive;
    }

    public void setConsecutiveIdleTrigger(Integer minConsecutiveIdle) {
        this.consecutiveIdleTrigger = minConsecutiveIdle;
    }

    public void setReceiveTimeout(Long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    public void setDeBatchingEnabled(final Boolean deBatchingEnabled) {
        this.deBatchingEnabled = deBatchingEnabled;
    }

    @Override
    protected CustomSimpleMessageListenerContainer createContainerInstance() {
        return new CustomSimpleMessageListenerContainer();
    }

    @Override
    protected void initializeContainer(CustomSimpleMessageListenerContainer instance, RabbitListenerEndpoint endpoint) {
        super.initializeContainer(instance, endpoint);

        if (this.txSize != null) {
            instance.setTxSize(this.txSize);
        }
        String concurrency = endpoint.getConcurrency();
        if (concurrency != null) {
            instance.setConcurrency(concurrency);
        }
        else if (this.concurrentConsumers != null) {
            instance.setConcurrentConsumers(this.concurrentConsumers);
        }
        if ((concurrency == null || !(concurrency.contains("-"))) && this.maxConcurrentConsumers != null) {
            instance.setMaxConcurrentConsumers(this.maxConcurrentConsumers);
        }
        if (this.startConsumerMinInterval != null) {
            instance.setStartConsumerMinInterval(this.startConsumerMinInterval);
        }
        if (this.stopConsumerMinInterval != null) {
            instance.setStopConsumerMinInterval(this.stopConsumerMinInterval);
        }
        if (this.consecutiveActiveTrigger != null) {
            instance.setConsecutiveActiveTrigger(this.consecutiveActiveTrigger);
        }
        if (this.consecutiveIdleTrigger != null) {
            instance.setConsecutiveIdleTrigger(this.consecutiveIdleTrigger);
        }
        if (this.receiveTimeout != null) {
            instance.setReceiveTimeout(this.receiveTimeout);
        }
        if (this.deBatchingEnabled != null) {
            instance.setDeBatchingEnabled(this.deBatchingEnabled);
        }
        //将RabbitListenerEndpoint设置到ListenerContainer
        instance.setEndpoint(endpoint);
    }

}
