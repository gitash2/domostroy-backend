package domostroy.core.application.offers;

import domostroy.aggregates.offer.domain.OfferAggregate;

public interface OfferRepository {
    OfferAggregate save(OfferAggregate aggregate);
    OfferAggregate getOfferById(Long offerId);
    void deleteOffer(Long offerId);
}
