package domostroy.core.application.cloudStorage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "yandex.cloud.storage")
@Getter
@Setter
public class YandexCloudProperties {
    public String accessKey = "";
    public String secretKey = "";
    public String endpoint = "";
    public String region = "";
    public String bucket = "";
}
