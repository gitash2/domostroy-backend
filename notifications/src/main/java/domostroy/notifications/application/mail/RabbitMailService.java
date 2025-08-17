package domostroy.notifications.application.mail;

import domostroy.events.mail.RentRequestStatusChangedEvent;
import domostroy.events.mail.UserRegisteredEvent;
import domostroy.notifications.adapters.adaptersInput.messaging.rabbit.config.RabbitMQConfigConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
@ConditionalOnProperty(name = "messaging.type", havingValue = "rabbitMQ")
public class RabbitMailService implements MessageSenderService {
    private final JavaMailSender mailSender;

    @Override
    @RabbitListener(queues = RabbitMQConfigConstants.Queue.QUEUE_CONFIRMATION_EMAIL)
    public void handleUserRegistration(UserRegisteredEvent event) {
        sendConfirmationEmail(event.email(), event.confirmationCode());
    }

    @Override
    @RabbitListener(queues = RabbitMQConfigConstants.Queue.QUEUE_CHANGE_REQUEST_STATUS)
    public void handleRequestChangeStatus(RentRequestStatusChangedEvent event) {
        sendChangeRequestStatusEmail(event.email(), event.title(), event.changedStatus());
    }

    @Override
    @RabbitListener(queues = RabbitMQConfigConstants.Queue.QUEUE_RESPONSE_TO_OFFER)
    public void handleRequestResponseToOffer(RentRequestStatusChangedEvent event) {
        sendNewResponseToOfferEmail(event.email(), event.title());
    }

    private void sendConfirmationEmail(String email, String code) {
        log.info("Sending confirmation email to " + email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Подтверждение регистрации");

        message.setText("""
            Здравствуйте!

            Спасибо за регистрацию в нашем сервисе.
            Пожалуйста, подтвердите вашу почту, введя следующий код:

            🔐 Код подтверждения: %s

            Если вы не регистрировались, просто проигнорируйте это письмо.

            С уважением,
            Команда сервиса Домострой
            """.formatted(code));

        mailSender.send(message);
    }


    private void sendChangeRequestStatusEmail(String email, String title, String changedStatus) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Статус вашей заявки обновлён");

        message.setText("""
        Здравствуйте!

        Мы хотим сообщить, что статус вашей заявки изменился.

        📄 Заявка: %s
        📌 Новый статус: %s

        Вы можете ознакомиться с подробностями, войдя в ваш личный кабинет.

        Спасибо, что пользуетесь нашим сервисом!

        С уважением,
        Команда сервиса Домострой
        """.formatted(title, changedStatus));

        mailSender.send(message);
    }


    private void sendNewResponseToOfferEmail(String email, String offerTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Отклик на ваше объявление");

        message.setText("""
            Здравствуйте!

            У вас новый отклик на ваше объявление:

            📌 "%s"

            Чтобы просмотреть детали и ответить — перейдите в личный кабинет.

            Мы рады, что ваш товар или услуга вызывает интерес!

            С уважением,
            Команда сервиса Домострой
            """.formatted(offerTitle));

        mailSender.send(message);
    }
}
