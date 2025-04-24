package domostroy.core.adapters.adaptersInput.dto.input.offers;

import domostroy.aggregates.offer.domain.OfferAggregate;

import java.time.LocalDateTime;
import java.util.Collection;

public record OfferDTO(
        Long id,
        String title,
        String description,
        String category,
        String currency,
        Double price,
        LocalDateTime createdAt,
        Integer cityId,
        Long userId,
        Collection<String> photos
) {
    public OfferDTO(OfferAggregate offer, Collection<String> photos) {
        this (
                offer.getOfferId(),
                offer.getTitle(),
                offer.getDescription(),
                offer.getCategory(),
                offer.getCurrency(),
                offer.getPrice(),
                offer.getCreatedAt(),
                offer.getCityId(),
                offer.getUserId(),
                photos
        );
    }
}
