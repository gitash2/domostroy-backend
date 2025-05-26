package domostroy.core.application.cloudStorage;

import domostroy.aggregates.offer.domain.OfferPhoto;
import domostroy.aggregates.offer.domain.OfferPhotoPath;
import domostroy.core.adapters.adaptersOutput.offerPhotos.projections.OfferPhotoProjection;

import java.util.List;

public interface OfferPhotoRepository {
    OfferPhoto save(OfferPhoto aggregate);
    OfferPhoto findById(Long id);
    List<OfferPhotoProjection> saveAll(List<OfferPhotoProjection> offerPhotos);
    List<OfferPhotoProjection> findAllByOfferId(Long offerId);
    List<String> findAllPhotoPathsByOfferId(Long offerId);
    String findFirstPhotoPathByOfferId(Long offerId);
    List<OfferPhotoPath> findFirstPhotoPathByOfferId(List<Long> offerIds);
    void deleteAllPhotosByOfferId(Long offerId);
    void deleteAllPhotosNotInList(List<Long> photoIds, Long offerId);


}
