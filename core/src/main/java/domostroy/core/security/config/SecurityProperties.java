package domostroy.core.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class SecurityProperties {

    public String secret = "";
    public Integer expiration = 600;

    public Integer strength = 10;
}
