package domostroy.notifications.application.mail;

import domostroy.events.mail.UserRegisteredEvent;
import domostroy.notifications.adapters.adaptersInput.messaging.config.RabbitMQConfigConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class MailService {
    private final JavaMailSender mailSender;

    @RabbitListener(queues = RabbitMQConfigConstants.Queue.QUEUE_SEND_CONFIRMATION_EMAIL)
    public void handleUserRegistration(UserRegisteredEvent event) {
        log.info("ura");
        sendConfirmationEmail(event.email(), event.confirmationCode());
    }

    private void sendConfirmationEmail(String email, String code) {
        log.info("Sending confirmation email to " + email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Confirm Registration");
        message.setText("Enter this confirmation code: " + code);
        mailSender.send(message);
    }
}
