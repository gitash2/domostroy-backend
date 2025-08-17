package domostroy.notifications.application.mail;

import domostroy.events.mail.RentRequestStatusChangedEvent;
import domostroy.events.mail.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@KafkaListener(
        topics = "${spring.kafka.consumer.topics.user-registered}"
)
@ConditionalOnProperty(name = "messaging.type", havingValue = "kafka")
public class KafkaMailService implements MessageSenderService {
    private final JavaMailSender mailSender;

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


    @Override
    @KafkaHandler
    public void handleUserRegistration(UserRegisteredEvent event) {
        log.info("Received user registered event: {}", event.email());
        sendConfirmationEmail(event.email(), event.confirmationCode());
    }

    @Override
    public void handleRequestChangeStatus(RentRequestStatusChangedEvent event) {

    }

    @Override
    public void handleRequestResponseToOffer(RentRequestStatusChangedEvent event) {

    }
}
