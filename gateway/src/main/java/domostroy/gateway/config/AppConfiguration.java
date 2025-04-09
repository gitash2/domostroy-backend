package domostroy.gateway.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;

@Configuration
public class AppConfiguration {

    @Bean
    ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .featuresToEnable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .build();
    }

    @Bean
    public Set<SwaggerUrl> swaggerApis(
            RouteDefinitionLocator locator,
            SwaggerUiConfigParameters swaggerUiConfigParameters
    ) {
        Set<SwaggerUrl> urls = new LinkedHashSet<>();
        List<RouteDefinition> routes = locator.getRouteDefinitions().collectList().block();

        assert routes != null;
        for (RouteDefinition route : routes) {
            String routeName = route.getId().replace("domostroy-", "");
            SwaggerUrl url = new SwaggerUrl(
                    routeName,
                    DEFAULT_API_DOCS_URL + "/" + routeName,
                    null
            );
            urls.add(url);
        }

        swaggerUiConfigParameters.setUrls(urls);
        return urls;
    }
}
