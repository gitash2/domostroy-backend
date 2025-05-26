package domostroy.core.adapters.adaptersOutput.rentRequest.dao;

import domostroy.core.adapters.adaptersOutput.rentRequest.projections.RentRequestProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RentRequestDAO extends JpaRepository<RentRequestProjection, Long> {
    @Query("""
          select r
          from RentRequestProjection r
          where r.offerId in (:ids)
    """)
    Page<RentRequestProjection> findAllIncomingRequests(List<Long> ids, Pageable pageable);


    @Query("""
                  select r
                  from RentRequestProjection r
                  where r.userId = :userId
            """)
    Page<RentRequestProjection> findAllOutgoingRequests(Long userId, Pageable pageable);

    void deleteAllByOfferId(Long offerId);

    @Query("""
    select distinct r
      from RentRequestProjection r
      join r.dates d
     where r.offerId = :offerId
       and d.date in :dates
  """)
    List<RentRequestProjection> findAllByOfferIdAndDatesIn(
            @Param("offerId") Long offerId,
            @Param("dates")    List<LocalDate> dates
    );

    @Modifying
    @Query(value = """
        delete from rent_request_dates rrd
        where rrd.rent_request_id = :rentRequestId
    """, nativeQuery = true)
    void deleteRentRequestDatesByRentRequestId(Long rentRequestId);


    @Query("""
           select count(r) > 0
           from RentRequestProjection r
           where r.userId = :userId and r.id = :rentRequestId
    """)
    boolean isMyRentRequest(Long rentRequestId, Long userId);

    @Query("""
        select rr.id
        from RentRequestProjection rr
        where rr.userId = :userId
    """)
    List<Long> findAllRentRequestIdsByUserId(Long userId);
}
