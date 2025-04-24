package domostroy.core.adapters.adaptersOutput.offers.projections;

import domostroy.aggregates.offer.domain.OfferAggregate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "offers")
@NoArgsConstructor
public class OfferProjection {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String description;

    private Double price;

    private String category;

    private String currency;

    private Integer cityId;

    private Long userId;

    private LocalDateTime createdAt;

    public OfferProjection(OfferAggregate aggregate) {
        id = aggregate.getOfferId();
        title = aggregate.getTitle();
        description = aggregate.getDescription();
        price = aggregate.getPrice();
        category = aggregate.getCategory();
        currency = aggregate.getCurrency();
        cityId = aggregate.getCityId();
        userId = aggregate.getUserId();
        createdAt = LocalDateTime.now();
    }

    public OfferAggregate toAggregate() {
        return new OfferAggregate(
                id,
                title,
                description,
                category,
                currency,
                price,
                cityId,
                userId,
                createdAt
        );
    }
}
