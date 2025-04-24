package domostroy.aggregates.offer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OfferCalendarAggregate {
    Long id;
    LocalDate date;
    Long offerId;
    boolean isBooked;
}
