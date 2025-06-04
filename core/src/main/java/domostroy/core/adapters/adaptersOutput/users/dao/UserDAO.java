package domostroy.core.adapters.adaptersOutput.users.dao;

import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.LessorInfo;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDAO extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("""
                  SELECT u
                  FROM User u
                  WHERE u.id IN :ids
            """)
    List<User> findAllByIds(List<Long> ids);

    @Modifying
    @Query(value = """
        delete from favourites
        where user_id = :userId
    """, nativeQuery = true)
    void deleteAllFavouritesByUserId(Long userId);

    @Modifying
    @Query(value = """
        delete from rent_request_dates
        where rent_request_id  in (:requestIds)
    """, nativeQuery = true)
    void deleteRentRequestDatesByRequestIds(List<Long> requestIds);

    @Modifying
    @Query("""
        delete from OfferCalendarProjection ocp
        where ocp.offerId in (:offerIds)
    """)
    void deleteOfferCalendarsByOfferIds(List<Long> offerIds);

    @Modifying
    @Query("""
    delete from RentRequestProjection rrp
    where rrp.offerId in (
        select o.id
        from OfferProjection o
        where o.userId = :userId
    )
""")
    void deleteRentRequestByUserId(Long userId);

    @Modifying
    @Query("""
        delete from OfferPhotoProjection rpp
        where rpp.offerId in (:offerIds)
    """)
    void deleteOffersPhotosByOfferIds(List<Long> offerIds);

    @Modifying
    @Query("""
        delete from OfferProjection op
        where op.userId = :userId
    """)
    void deleteOffersByUserId(Long userId);

    @Modifying
    @Query("""
        delete from User u
        where u.id = :userId
    """)
    void deleteUserByUserId(Long userId);

    @Query("""
        SELECT u FROM User u
        WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%'))
        """)
    Page<User> searchUsers(@Param("query") String query, Pageable pageable);

    @Query("""
        SELECT u FROM User u
        where u.id = (select o.userId from OfferProjection o where o.id = :offerId)
    """)
    User findUserByOfferId(Long offerId);

}
