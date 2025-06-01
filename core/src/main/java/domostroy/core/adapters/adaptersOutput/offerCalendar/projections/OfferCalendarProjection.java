package domostroy.core.adapters.adaptersOutput.offerCalendar.projections;

import domostroy.core.adapters.adaptersOutput.rentRequest.projections.RentRequestProjection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(mappedBy = "dates")
    private Set<RentRequestProjection> rentRequests;
}
