package domostroy.core.adapters.adaptersOutput.offerCalendar.dao;

import domostroy.core.adapters.adaptersOutput.offerCalendar.projections.OfferCalendarProjection;
import domostroy.core.application.offerCalendar.OfferCalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

    @Override
    public boolean areDatesBooked(Set<LocalDate> dates, Long offerId) {
        return offerCalendarDAO.areDatesBooked(dates, offerId);
    }

    @Override
    public Set<OfferCalendarProjection> getOfferDates(Long offerId, Set<LocalDate> dates) {
        return offerCalendarDAO.getOfferDates(offerId, dates);
    }

    @Override
    public List<OfferCalendarProjection> findOfferDates(Long offerId) {
        return offerCalendarDAO.findOfferDatesByOfferId(offerId);
    }
}
