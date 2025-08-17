package domostroy.auth.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.kafka.producer.topics")
@Getter
@Setter
public class KafkaTopicsConfig {
    private String userRegistered;
}
