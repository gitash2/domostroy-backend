package domostroy.notifications.adapters.adaptersInput.messaging.config;

public class RabbitMQConfigConstants {
    public static final String EXCHANGE_NAME = "user.registration.exchange";

    public static final class Queue{
        public static final String QUEUE_CONFIRMATION_EMAIL = "user.registration.queue";
        public static final String QUEUE_CHANGE_REQUEST_STATUS = "user.change.request.status.queue";
        public static final String QUEUE_RESPONSE_TO_OFFER = "user.offer.response.queue";
    }
}
