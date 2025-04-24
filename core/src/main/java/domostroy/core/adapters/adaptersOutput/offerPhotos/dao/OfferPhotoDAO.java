package domostroy.core.adapters.adaptersOutput.offerPhotos.dao;

import domostroy.aggregates.offer.domain.OfferPhoto;
import domostroy.core.adapters.adaptersOutput.offerPhotos.projections.OfferPhotoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferPhotoDAO extends JpaRepository<OfferPhotoProjection, Long> {


    List<OfferPhoto> findAllByOfferId(Long offerId);


    @Query("""
            select o.imagePath
            from OfferPhotoProjection o
            where o.offerId = :offerId
            """)
    List<String> findAllPhotoPathsByOfferId(Long offerId);
}
