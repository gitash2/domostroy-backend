package domostroy.core.adapters.adaptersOutput.offers.dao;

import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface OfferDAO extends JpaRepository<OfferProjection, Long>, JpaSpecificationExecutor<OfferProjection>, PagingAndSortingRepository<OfferProjection, Long> {
    Page<OfferProjection> getOffersByUserId(Long userId, Pageable pageable);

    @Query(
            value = """
    SELECT o
      FROM OfferProjection o
      JOIN o.favouredBy u
     WHERE u.id = :userId
    """,
            countQuery = """
    SELECT count(o)
      FROM OfferProjection o
      JOIN o.favouredBy u
     WHERE u.id = :userId
  """
    )
    Page<OfferProjection> findFavouriteOffersByUserId(
            @Param("userId") Long userId,
            Pageable pageable
    );



    @Query("""
            select count(o) != 0
            from OfferProjection o
            join User u on u.email =:username
            where o.id = :offerId
            """)
    boolean isMyOffer(Long offerId, String username);

    @Query(value = """
                    select count(f) != 0
                    from favourites f
                    join users u on u.email =:username
                    where f.offer_id = :offerId
            """, nativeQuery = true)
    boolean isFavourite(Long offerId, String username);

    @Query("""
                    select count(o)
                    from OfferProjection o
                    where o.userId = :userId
            """)
    int getMyOffersCount(Long userId);
}
