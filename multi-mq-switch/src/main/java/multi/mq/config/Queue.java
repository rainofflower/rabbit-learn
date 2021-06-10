package multi.mq.config;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class Queue implements Serializable {

    private static final long serialVersionUID = -1;

    private  String name;
    private  boolean durable;
    private  boolean exclusive;
    private  boolean autoDelete;
    private  Map<String, Object> arguments;
    
}
