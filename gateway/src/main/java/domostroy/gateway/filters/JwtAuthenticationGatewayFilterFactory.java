package domostroy.gateway.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domostroy.gateway.Constants;
import domostroy.gateway.exceptions.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    private final WebClient.Builder webClient;
    private final ObjectMapper objectMapper;
    private final Environment env;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            if (exchange.getRequest().getURI().getPath().contains("/v3/api-docs")) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange.getResponse(), Constants.NO_AUTH_MESSAGE);
            }

            return webClient.build()
                    .post()
                    .uri(getAuthURI())
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .retrieve()
                    .toEntity(Boolean.class)
                    .flatMap(response -> {
                        if (Boolean.TRUE.equals(response.getBody())) {
                            return chain.filter(exchange);
                        } else {
                            return onError(exchange.getResponse(), "Invalid token");
                        }
                    })
                    .onErrorResume(ex ->
                            onError(exchange.getResponse(), ex.getMessage())
                    );
        };
    }

    private String getAuthURI() {
        if (Arrays.asList(env.getActiveProfiles()).contains("local")) {
            return "http://localhost:8081/auth/validate";
        } else if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
            return "http://changeLaterToTheHostName:8081/auth/validate";
        } else if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {
            return "http://auth:8081/auth/validate";
        }
        throw new IllegalStateException("No valid profile found. Current profiles: "
                + Arrays.toString(env.getActiveProfiles()));
    }

    private Mono<Void> onError(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            ExceptionResponse exResponse = new ExceptionResponse(
                    HttpStatus.UNAUTHORIZED,
                    message,
                    response.getHeaders().getLocation() != null ?
                            response.getHeaders().getLocation().getPath() : ""
            );

            byte[] bytes = objectMapper.writeValueAsBytes(exResponse);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
}
