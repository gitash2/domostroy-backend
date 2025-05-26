package domostroy.core.adapters.adaptersOutput.rentRequest.projections;

import domostroy.aggregates.rentRequest.RentRequestStatus;
import domostroy.core.adapters.adaptersOutput.offerCalendar.projections.OfferCalendarProjection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "rent_request")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RentRequestProjection {
    @Id
    @GeneratedValue(generator = "rent_request_seq")
    private Long id;

    private Long offerId;
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime resolvedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "rent_request_status")
    private RentRequestStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "rent_request_dates",
            joinColumns = @JoinColumn(name = "rent_request_id"),
            inverseJoinColumns = @JoinColumn(name = "offer_date_id")
    )
    private Set<OfferCalendarProjection> dates;


}
