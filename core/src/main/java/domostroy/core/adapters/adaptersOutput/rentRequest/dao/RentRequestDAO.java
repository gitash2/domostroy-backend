package domostroy.core.adapters.adaptersOutput.rentRequest.dao;

import domostroy.core.adapters.adaptersOutput.rentRequest.projections.RentRequestProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
