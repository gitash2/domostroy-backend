package domostroy.aggregates.offer.domain;

import java.time.LocalDateTime;

public record OfferPhoto(
        Long id,
        Long offerId,
        String imageBucket,
        String imagePath,
        LocalDateTime createdAt
) {
}
