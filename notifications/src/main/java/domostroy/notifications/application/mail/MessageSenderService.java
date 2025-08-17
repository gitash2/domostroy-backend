package domostroy.notifications.application.mail;

import domostroy.events.mail.RentRequestStatusChangedEvent;
import domostroy.events.mail.UserRegisteredEvent;
import domostroy.notifications.adapters.adaptersInput.messaging.rabbit.config.RabbitMQConfigConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface MessageSenderService {
    void handleUserRegistration(UserRegisteredEvent event);

    void handleRequestChangeStatus(RentRequestStatusChangedEvent event);

    void handleRequestResponseToOffer(RentRequestStatusChangedEvent event);
}
