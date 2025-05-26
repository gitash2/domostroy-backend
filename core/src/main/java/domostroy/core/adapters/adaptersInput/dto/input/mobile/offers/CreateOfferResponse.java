package domostroy.core.adapters.adaptersInput.dto.input.mobile.offers;

import domostroy.aggregates.currency.Currency;
import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;

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
    public CreateOfferResponse(OfferProjection offer) {
        this(
                offer.getId(),
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
