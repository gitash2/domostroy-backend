package domostroy.core.application.deepseek.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ai.moderation")
@Getter
@Setter
public class LLMProperties {
    private String key = "";
}
