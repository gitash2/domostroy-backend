package domostroy.core.adapters.adaptersOutput.offerCalendar.projections;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "offer_calendar")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferCalendarProjection {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate date;
    private Long offerId;
    private boolean isBooked;
}
