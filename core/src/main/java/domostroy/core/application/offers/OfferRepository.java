package domostroy.core.application.offers;

import domostroy.aggregates.offer.domain.OfferAggregate;
import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface OfferRepository {
    OfferAggregate save(OfferAggregate aggregate);
    OfferProjection getOfferById(Long offerId);
    void deleteOffer(Long offerId);
    Page<OfferProjection> getMyOffers(Long userId, Pageable pageable);
    Page<OfferProjection> findAll(Specification<OfferProjection> spec, Pageable pageable);
    Page<OfferProjection> findFavouriteOffersByUserId(Long userId, Pageable pageable);
    boolean isMyOffer(Long offerId, String email);
    boolean isFavourite(Long offerId, String email);
    int getMyOffersCount(Long userId);
    List<OfferProjection> getMyOffersIds(Long userId);
    List<OfferProjection> findAllByIds(List<Long> offerIds);
}
