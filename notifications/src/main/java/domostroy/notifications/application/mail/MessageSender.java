package domostroy.notifications.application.mail;

import domostroy.events.mail.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageSender {
    private final MessageSenderService mailSender;

    public void sendConfirmationCode(UserRegisteredEvent event) {
        mailSender.handleUserRegistration(event);
    }

}
