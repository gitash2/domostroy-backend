package domostroy.core.adapters.adaptersInput.dto.input.mobile.offers;

import domostroy.aggregates.currency.Currency;
import domostroy.aggregates.offer.domain.OfferAggregate;

import java.time.LocalDateTime;

public record CreateOfferResponse(
        Long offerId,
        String title,
        String description,
        Integer categoryId,
        Currency currency,
        Double price,
        Integer cityId,
        LocalDateTime createdAt
) {
    public CreateOfferResponse(OfferAggregate offer) {
        this(offer.getOfferId(),
                offer.getTitle(),
                offer.getDescription(),
                offer.getCategoryId(),
                offer.getCurrency(),
                offer.getPrice(),
                offer.getCityId(),
                offer.getCreatedAt()
        );
    }
}
