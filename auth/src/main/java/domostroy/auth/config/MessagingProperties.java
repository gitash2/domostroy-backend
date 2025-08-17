package domostroy.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "messaging")
@Getter
@Setter
public class MessagingProperties {
    private String type;
}
