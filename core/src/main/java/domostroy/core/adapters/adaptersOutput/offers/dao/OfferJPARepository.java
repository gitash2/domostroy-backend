package domostroy.core.adapters.adaptersOutput.offers.dao;

import domostroy.aggregates.offer.domain.OfferAggregate;
import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import domostroy.core.application.offers.OfferRepository;
import domostroy.core.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfferJPARepository implements OfferRepository {
    private final OfferDAO offerDAO;

    @Override
    public OfferAggregate save(OfferAggregate aggregate) {
        OfferProjection projection = new OfferProjection(aggregate);
        return offerDAO.save(projection).toAggregate();
    }

    @Override
    public OfferAggregate getOfferById(Long offerId) {
        return offerDAO.findById(offerId).orElseThrow(
                () -> new ObjectNotFoundException("Offer with id" + offerId + "is not found")).toAggregate();
    }

    @Override
    public void deleteOffer(Long offerId) {
        offerDAO.deleteById(offerId);

    }
}
