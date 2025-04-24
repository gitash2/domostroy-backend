package domostroy.aggregates.offer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OfferAggregate
{
    private Long offerId;
    private String title;
    private String description;
    private String category;
    private String currency;
    private Double price;
    private Integer cityId;
    private Long userId;
    private LocalDateTime createdAt;
}
