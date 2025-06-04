package domostroy.core.adapters.adaptersOutput.offers.dao;

import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OfferDAO extends JpaRepository<OfferProjection, Long>, JpaSpecificationExecutor<OfferProjection>, PagingAndSortingRepository<OfferProjection, Long> {
    Page<OfferProjection> getOffersByUserId(Long userId, Pageable pageable);

    @Query(
            value = """
    SELECT o
      FROM OfferProjection o
      JOIN o.favouredBy u
     WHERE u.id = :userId
     and o.isBanned = false
     and u.isBanned = false
    """,
            countQuery = """
    SELECT count(o)
      FROM OfferProjection o
      JOIN o.favouredBy u
     WHERE u.id = :userId
     and o.isBanned = false
     and u.isBanned = false
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
            and u.id = o.userId
            """)
    boolean isMyOffer(Long offerId, String username);

    @Query(value = """
    select exists (
        select 1
        from favourites f
        join users u on f.user_id = u.id
        where u.email = :username and f.offer_id = :offerId
    )
    """, nativeQuery = true)
    boolean isFavourite(@Param("offerId") Long offerId, @Param("username") String username);


    @Query("""
                    select count(o)
                    from OfferProjection o
                    where o.userId = :userId
                    and o.isBanned = false
            """)
    int getMyOffersCount(Long userId);


    @Query("""
                            select o
                            from OfferProjection o
                            where o.userId = :userId
            """)
    List<OfferProjection> getMyOffersIds(Long userId);

    Optional<OfferProjection> findOfferById(Long userId);

    @Query("""
        select o.id
        from OfferProjection o
        where o.userId = :userId
    """)
    List<Long> findAllOfferIdsByUserId(Long userId);
}
