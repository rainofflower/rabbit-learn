package multi.mq.config;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.SimpleResourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 动态mq切换切面
 */
@Aspect
@Component
public class DynamicMqAspect {

	@Autowired
	private ConnectionFactory rabbitConnectionFactory;

	@Before("@annotation(targetMq)  " +
			"&& !@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
	public void before(TargetMq targetMq) {
		String name = targetMq.value();
		SimpleResourceHolder.bind(rabbitConnectionFactory, name);
	}

	@After("@annotation(multi.mq.config.TargetMq) " +
			"&& !@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
	public void after(){
		SimpleResourceHolder.unbind(rabbitConnectionFactory);
	}
}