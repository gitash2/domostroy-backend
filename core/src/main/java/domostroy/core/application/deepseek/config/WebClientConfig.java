package domostroy.core.application.deepseek.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;

@Configuration
@EnableConfigurationProperties({LLMProperties.class})
@RequiredArgsConstructor
@Log4j2
public class WebClientConfig {
    private final LLMProperties properties;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getKey())
                .defaultHeader("HTTP-Referer", "http://localhost:8080") // Обязательный
                .defaultHeader("X-Title", "Domostroy") // Рекомендуемый
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
