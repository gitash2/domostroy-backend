package domostroy.core.adapters.adaptersOutput.offerCalendar.dao;

import domostroy.core.adapters.adaptersOutput.offerCalendar.projections.OfferCalendarProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferCalendarDAO extends JpaRepository<OfferCalendarProjection, Long> {
    void deleteAllByOfferId(Long offerId);
}
