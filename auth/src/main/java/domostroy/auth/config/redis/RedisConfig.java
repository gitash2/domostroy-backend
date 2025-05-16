package domostroy.auth.config.redis;

import domostroy.auth.dto.VerificationData;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableConfigurationProperties(RedisConfig.DataRedisProps.class)
public class RedisConfig {


    @Bean
    public RedisConnectionFactory redisConnectionFactory(DataRedisProps props) {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(props.getHost(), props.getPort()));
    }

    @ConfigurationProperties(prefix = "spring.data.redis")
    public static class DataRedisProps {
        private String host;
        private int port;
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
    }

    @Bean
    public RedisTemplate<String, VerificationData> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, VerificationData> tpl = new RedisTemplate<>();
        tpl.setConnectionFactory(cf);
        tpl.setKeySerializer(new StringRedisSerializer());
        tpl.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return tpl;
    }

}
