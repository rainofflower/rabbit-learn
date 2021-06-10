package multi.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

/**
 * ${DESCRIPTION}
 *
 * @author yanghui
 * @date 2021-06-09 22:23
 **/
@SpringBootApplication(exclude = {RabbitAutoConfiguration.class})
public class Application {

    public static void main(String... args){
        SpringApplication.run(Application.class, args);
    }
}
