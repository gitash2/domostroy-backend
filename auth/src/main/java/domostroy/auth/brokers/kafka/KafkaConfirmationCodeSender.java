package domostroy.auth.brokers.kafka;

import domostroy.auth.config.kafka.KafkaTopicsConfig;
import domostroy.auth.brokers.ConfirmationCodeSender;
import domostroy.events.mail.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConfirmationCodeSender implements ConfirmationCodeSender {

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @Override
    public void sendConfirmationCode(UserRegisteredEvent event) {
        kafkaTemplate.send(
                kafkaTopicsConfig.getUserRegistered(),
                event
        );
    }
}
