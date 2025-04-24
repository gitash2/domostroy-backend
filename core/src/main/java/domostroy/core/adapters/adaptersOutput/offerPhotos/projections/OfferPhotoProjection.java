package domostroy.core.adapters.adaptersOutput.offerPhotos.projections;

import domostroy.aggregates.offer.domain.OfferPhoto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "offer_images")
@Entity
@Getter
@Setter
public class OfferPhotoProjection {
    @Id
    @GeneratedValue
    private Long id;

    private Long offerId;

    private String imageBucket;

    private String imagePath;

    private LocalDateTime createdAt;

    public OfferPhotoProjection(OfferPhoto aggregate) {
        this.id = aggregate.id();
        this.offerId = aggregate.offerId();
        this.imageBucket = aggregate.imageBucket();
        this.imagePath = aggregate.imagePath();
        this.createdAt = aggregate.createdAt();
    }
}
