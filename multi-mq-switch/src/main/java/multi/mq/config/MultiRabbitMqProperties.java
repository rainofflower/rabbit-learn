package multi.mq.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 配置映射
 */
@ConfigurationProperties(prefix = "mq")
@Getter
@Setter
public class MultiRabbitMqProperties {

    private String defaultMq = "aaa";

    private Map<String, CustomRabbitProperties> mqMap = new LinkedHashMap<>();
}
