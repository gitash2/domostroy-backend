package domostroy.auth.brokers.rabbit;

import domostroy.auth.config.rabbit.RabbitMQConfigConstants;
import domostroy.auth.brokers.ConfirmationCodeSender;
import domostroy.events.mail.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "messaging.type", havingValue = "rabbitMQ")
public class RabbitMQConfirmationCodeSender implements ConfirmationCodeSender {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendConfirmationCode(UserRegisteredEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfigConstants.Queue.QUEUE_SEND_CONFIRMATION_EMAIL,
                new UserRegisteredEvent(
                        event.email(),
                        event.password(),
                        event.confirmationCode()));

    }
}
