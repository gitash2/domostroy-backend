package domostroy.core.adapters.adaptersInput.dto.input.offers;

import domostroy.aggregates.offer.domain.OfferAggregate;

import java.time.LocalDateTime;

public record CreateOfferResponse(
        Long offerId,
        String title,
        String description,
        String category,
        String currency,
        Double price,
        Integer cityId,
        LocalDateTime createdAt
) {
    public CreateOfferResponse(OfferAggregate offer) {
        this(offer.getOfferId(),
                offer.getTitle(),
                offer.getDescription(),
                offer.getCategory(),
                offer.getCurrency(),
                offer.getPrice(),
                offer.getCityId(),
                offer.getCreatedAt()
        );
    }
}
