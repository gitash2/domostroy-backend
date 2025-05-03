package domostroy.core.adapters.adaptersOutput.offers.dao;

import domostroy.aggregates.offer.domain.OfferAggregate;
import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import domostroy.core.application.offers.OfferRepository;
import domostroy.core.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public OfferProjection getOfferById(Long offerId) {
        return offerDAO.findById(offerId).orElseThrow(
                () -> new ObjectNotFoundException("Offer with id" + offerId + "is not found"));
    }

    @Override
    public void deleteOffer(Long offerId) {
        offerDAO.deleteById(offerId);

    }

    public Page<OfferProjection> getMyOffers(Long userId, Pageable pageable) {
        return offerDAO.getOffersByUserId(userId, pageable);
    }

    @Override
    public Page<OfferProjection> findAll(Specification<OfferProjection> spec, Pageable pageable) {
        return offerDAO.findAll(spec, pageable);
    }

    @Override
    public Page<OfferProjection> findRandomOffersWithSeed(String seed, Pageable pageable) {
        return offerDAO.findRandomOffersWithSeed(seed, pageable);
    }

    @Override
    public Page<OfferProjection> findFavouriteOffersByUserId(Long userId, Pageable pageable) {
        return offerDAO.findFavouriteOffersByUserId(userId, pageable);
    }

    @Override
    public boolean isMyOffer(Long offerId, String email) {
        return false;
    }

    @Override
    public boolean isFavourite(Long offerId, String email) {
        return offerDAO.isFavourite(offerId, email);
    }
}
