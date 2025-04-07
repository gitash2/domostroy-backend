package domostroy.auth.config.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitQueuesConfig {

    @Bean
    public Queue userRegisteredQueue() {
        return new Queue(RabbitMQConfigConstants.Queue.QUEUE_SEND_CONFIRMATION_EMAIL, true);
    }
}
