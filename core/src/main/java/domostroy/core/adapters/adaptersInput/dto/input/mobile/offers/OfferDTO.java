package domostroy.core.adapters.adaptersInput.dto.input.mobile.offers;

import domostroy.aggregates.currency.Currency;
import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;

import java.time.LocalDateTime;
import java.util.List;

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
        List<OfferPhoto> photos,
        boolean isFavourite,
        boolean isBanned,
        String banReason

) {
    public OfferDTO(OfferProjection offer, List<OfferPhoto> photos, boolean isFavourite) {
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
                isFavourite,
                offer.isBanned(),
                offer.getBanReason()
        );
    }
}
