package domostroy.core.application.offerCalendar;

import domostroy.core.adapters.adaptersOutput.offerCalendar.projections.OfferCalendarProjection;

import java.util.List;

public interface OfferCalendarRepository {
    List<OfferCalendarProjection> saveOfferDates(List<OfferCalendarProjection> offerCalendarAggregates);
    List<OfferCalendarProjection> bookOfferDates(List<OfferCalendarProjection> offerCalendarAggregates);
    void deleteAllByOfferId(Long offerId);
}