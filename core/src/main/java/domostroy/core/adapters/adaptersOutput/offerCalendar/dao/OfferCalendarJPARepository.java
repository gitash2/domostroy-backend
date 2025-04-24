package domostroy.core.adapters.adaptersOutput.offerCalendar.dao;

import domostroy.aggregates.offer.domain.OfferCalendarAggregate;
import domostroy.core.adapters.adaptersOutput.offerCalendar.projections.OfferCalendarProjection;
import domostroy.core.application.offerCalendar.OfferCalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OfferCalendarJPARepository implements OfferCalendarRepository {
    private final OfferCalendarDAO offerCalendarDAO;

    @Override
    public List<OfferCalendarProjection> saveOfferDates(List<OfferCalendarProjection> dates) {
        return offerCalendarDAO.saveAll(dates);
    }

    @Override
    public List<OfferCalendarProjection> bookOfferDates(List<OfferCalendarProjection> dates) {
        dates.forEach(date -> date.setBooked(true));
        return offerCalendarDAO.saveAll(dates);
    }

    @Override
    public void deleteAllByOfferId(Long offerId) {
        offerCalendarDAO.deleteAllByOfferId(offerId);
    }
}
