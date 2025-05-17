package domostroy.core.adapters.adaptersOutput.offerPhotos.dao;

import domostroy.aggregates.offer.domain.OfferPhoto;
import domostroy.aggregates.offer.domain.OfferPhotoPath;
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


    @Query("""
            select o.imagePath
            from OfferPhotoProjection o
            where o.offerId = :offerId
            order by o.createdAt asc
            limit 1
    """)
    String findFirstByOfferId(Long offerId);


    @Query(value = """
        SELECT DISTINCT ON (op.offer_id)
           op.offer_id    AS offerId,
           op.image_path  AS imagePath
        FROM offer_images op
        WHERE op.offer_id IN :offerIds
        ORDER BY op.offer_id, op.created_at ASC
        """,
            nativeQuery = true)
    List<OfferPhotoPath> findFirstPhotoPathByOfferId(List<Long> offerIds);

    void deleteAllByOfferId(Long offerId);
}
