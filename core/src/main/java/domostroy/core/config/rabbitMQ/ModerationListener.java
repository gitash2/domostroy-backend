package domostroy.core.config.rabbitMQ;

import domostroy.core.adapters.adaptersInput.dto.output.offers.ModerationResponse;
import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import domostroy.core.application.deepseek.LLMService;
import domostroy.core.application.deepseek.ModerationPayload;
import domostroy.core.application.offers.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ModerationListener {

    private final OfferRepository offerRepository;
    private final LLMService llmService;

    @RabbitListener(queues = RabbitMQConfigConstants.Queue.QUEUE_MODERATION_OFFER)
    @Transactional
    public void moderate(ModerationPayload payload) {
        ModerationResponse response = llmService.checkText(payload.title(), payload.description()).block();

        OfferProjection offer = offerRepository.findById(payload.offerId());

        if (response == null || response.getValid()) {
            offer.setBanned(false);
            offer.setBanReason(null);
        } else {
            offer.setBanned(true);
            offer.setBanReason(response.getReason());
        }

        offerRepository.save(offer);
    }
}
