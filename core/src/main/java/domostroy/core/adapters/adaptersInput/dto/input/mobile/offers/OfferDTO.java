package domostroy.core.adapters.adaptersInput.dto.input.mobile.offers;

import domostroy.aggregates.currency.Currency;
import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;

import java.time.LocalDateTime;
import java.util.Collection;

public record OfferDTO(
        Long id,
        String title,
        String description,
        Integer category,
        Currency currency,
        Double price,
        LocalDateTime createdAt,
        Integer cityId,
        Long userId,
        Collection<String> photos,
        boolean isFavourite

) {
    public OfferDTO(OfferProjection offer, Collection<String> photos, boolean isFavourite) {
        this (
                offer.getId(),
                offer.getTitle(),
                offer.getDescription(),
                offer.getCategoryId(),
                offer.getCurrency(),
                offer.getPrice(),
                offer.getCreatedAt(),
                offer.getCityId(),
                offer.getUserId(),
                photos,
                isFavourite
        );
    }
}
