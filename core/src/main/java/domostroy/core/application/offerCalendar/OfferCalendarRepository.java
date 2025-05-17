package domostroy.core.application.offerCalendar;

import domostroy.core.adapters.adaptersOutput.offerCalendar.projections.OfferCalendarProjection;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface OfferCalendarRepository {
    List<OfferCalendarProjection> saveOfferDates(List<OfferCalendarProjection> offerCalendarAggregates);
    List<OfferCalendarProjection> bookOfferDates(List<OfferCalendarProjection> offerCalendarAggregates);
    void deleteAllByOfferId(Long offerId);
    boolean areDatesBooked(Set<LocalDate> dates, Long offerId);
    Set<OfferCalendarProjection> getOfferDates(Long offerId, Set<LocalDate> dates);
    List<OfferCalendarProjection> findOfferDates(Long offerId);
}