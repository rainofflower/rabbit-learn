package multi.mq.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 接收规则结果
 */
@Slf4j
@Component
public class RabbitMqReceiver {



    @TargetMq("aaa")
    @RabbitListener(queues = {"${mq.mqMap.aaa.queues[0].name}"},
            containerFactory = "rabbitListenerContainerFactory")
    public void recevie1(Message msg) {
        String message = new String(msg.getBody());

        try{

        }catch (Exception e) {
            log.error("RabbitMqReceiver#recevie1处理消息失败", e);
        }
    }

    @TargetMq("bbb")
    @RabbitListener(queues = {"${mq.mqMap.bbb.queues[0].name}"},
            containerFactory = "rabbitListenerContainerFactory")
    public void recevie2(Message msg) {
        String message = new String(msg.getBody(), StandardCharsets.UTF_8);

        try{

        } catch (Exception e) {
            log.error("RabbitMqReceiver#recevie2处理消息失败", e);
        }
    }

}
