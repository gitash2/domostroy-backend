package domostroy.core.application.cloudStorage.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({YandexCloudProperties.class})
public class YandexCloudConfig {
    private final YandexCloudProperties properties;

    @Bean
    public AwsBasicCredentials credentials() {
        return AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey());
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(properties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials()))
                .endpointOverride(URI.create(properties.getEndpoint()))
                .build();
    }
}
