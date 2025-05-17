package domostroy.core.adapters.adaptersOutput.offerPhotos.dao;

import domostroy.aggregates.offer.domain.OfferPhoto;
import domostroy.aggregates.offer.domain.OfferPhotoPath;
import domostroy.core.adapters.adaptersOutput.offerPhotos.projections.OfferPhotoProjection;
import domostroy.core.application.cloudStorage.OfferPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OfferPhotoJPARepository implements OfferPhotoRepository {

    private final OfferPhotoDAO offerPhotoDAO;
    @Override
    public OfferPhoto save(OfferPhoto aggregate) {
        OfferPhotoProjection projection = new OfferPhotoProjection(
                aggregate
        );

        offerPhotoDAO.save(projection);
        return aggregate;
    }

    @Override
    public OfferPhoto findById(Long id) {
        return null;
    }

    @Override
    public List<OfferPhoto> saveAll(List<OfferPhoto> offerPhotos) {
        List<OfferPhotoProjection> projections = offerPhotos.stream().map(OfferPhotoProjection::new).toList();
        offerPhotoDAO.saveAll(projections);
        return offerPhotos;
    }

    @Override
    public List<OfferPhoto> findAllByOfferId(Long offerId) {
        return offerPhotoDAO.findAllByOfferId(offerId);
    }

    @Override
    public List<String> findAllPhotoPathsByOfferId(Long offerId) {
        return offerPhotoDAO.findAllPhotoPathsByOfferId(offerId);
    }

    @Override
    public String findFirstPhotoPathByOfferId(Long offerId) {
        return offerPhotoDAO.findFirstByOfferId(offerId);
    }

    @Override
    public List<OfferPhotoPath> findFirstPhotoPathByOfferId(List<Long> offerIds) {
        return offerPhotoDAO.findFirstPhotoPathByOfferId(offerIds);
    }

    @Override
    public void deleteAllPhotosByOfferId(Long offerId) {
        offerPhotoDAO.deleteAllByOfferId(offerId);
    }
}
