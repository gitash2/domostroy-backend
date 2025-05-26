package domostroy.core.config.rabbitMQ;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rabbitmq")
@Getter
@Setter
public class RabbitMQConnectionConfig {
    public String host;
    public Integer port;
    public String username;
    public String password;
}
