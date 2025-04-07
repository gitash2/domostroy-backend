package domostroy.auth.config.rabbit;

public class RabbitMQConfigConstants {
    public static final String EXCHANGE_NAME = "user.registration.exchange";

    public static final class Queue{
        public static final String QUEUE_SEND_CONFIRMATION_EMAIL = "user.registration.queue";
    }
}
