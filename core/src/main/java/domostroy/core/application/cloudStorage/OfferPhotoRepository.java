package domostroy.core.application.cloudStorage;

import domostroy.aggregates.offer.domain.OfferPhoto;

import java.util.List;

public interface OfferPhotoRepository {
    OfferPhoto save(OfferPhoto aggregate);
    OfferPhoto findById(Long id);
    List<OfferPhoto> saveAll(List<OfferPhoto> offerPhotos);
    List<OfferPhoto> findAllByOfferId(Long offerId);
    List<String> findAllPhotoPathsByOfferId(Long offerId);

}
