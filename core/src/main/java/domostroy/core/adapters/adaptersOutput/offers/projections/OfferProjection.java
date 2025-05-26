package domostroy.core.adapters.adaptersOutput.offers.projections;

import domostroy.aggregates.currency.Currency;
import domostroy.aggregates.offer.domain.OfferAggregate;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "offers")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    private LocalDateTime createdAt;

    private boolean isBanned;

    private String banReason;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OfferProjection that = (OfferProjection) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
