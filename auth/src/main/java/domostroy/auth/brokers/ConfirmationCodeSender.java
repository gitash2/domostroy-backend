package domostroy.auth.brokers;

import domostroy.events.mail.UserRegisteredEvent;

public interface ConfirmationCodeSender {
    void sendConfirmationCode(UserRegisteredEvent event);
}
