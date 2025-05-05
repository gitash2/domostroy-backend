package domostroy.core.adapters.adaptersOutput.offers.projections;

import domostroy.aggregates.currency.Currency;
import domostroy.aggregates.offer.domain.OfferAggregate;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "offers")
@NoArgsConstructor
@Getter
public class OfferProjection {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String description;

    private Double price;

    private Integer categoryId;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "favourites")
    Set<User> favouredBy;

    private Integer cityId;

    private Long userId;

    private LocalDateTime createdAt;

    public OfferProjection(OfferAggregate aggregate) {
        id = aggregate.getOfferId();
        title = aggregate.getTitle();
        description = aggregate.getDescription();
        price = aggregate.getPrice();
        categoryId = aggregate.getCategoryId();
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
                categoryId,
                currency,
                price,
                cityId,
                userId,
                createdAt
        );
    }
}
