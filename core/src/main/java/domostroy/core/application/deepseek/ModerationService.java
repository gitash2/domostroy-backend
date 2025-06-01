package domostroy.core.application.deepseek;

import domostroy.core.config.rabbitMQ.RabbitMQConfigConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ModerationService {
    private final RabbitTemplate rabbitTemplate;

    public void sendForModeration(Long offerId, String title, String description) {
        ModerationPayload payload = new ModerationPayload(offerId, title, description);
        log.info("Sending payload to moderation queue: {}", payload);
        rabbitTemplate.convertAndSend(RabbitMQConfigConstants.Queue.QUEUE_MODERATION_OFFER, payload);
    }
}
