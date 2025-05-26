package domostroy.core.config.rabbitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitQueuesConfig {

    @Bean
    public Queue userRegisteredQueue() {
        return new Queue(RabbitMQConfigConstants.Queue.QUEUE_CONFIRMATION_EMAIL, true);
    }

    @Bean
    public Queue rentRequestStatusChangedQueue() {
        return new Queue(RabbitMQConfigConstants.Queue.QUEUE_CHANGE_REQUEST_STATUS, true);
    }

    @Bean
    public Queue OfferResponseQueue() {
        return new Queue(RabbitMQConfigConstants.Queue.QUEUE_RESPONSE_TO_OFFER, true);
    }
}
