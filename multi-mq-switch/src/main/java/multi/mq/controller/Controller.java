package multi.mq.controller;

import multi.mq.config.MultiRabbitMqProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ${DESCRIPTION}
 *
 * @author yanghui
 * @date 2021-06-10 11:12
 **/
@RestController
@RequestMapping("mq")
public class Controller {

    @Autowired
    private MultiRabbitMqProperties properties;



    @GetMapping("getProperties")
    public MultiRabbitMqProperties getProperties(){
        return properties;
    }
}
