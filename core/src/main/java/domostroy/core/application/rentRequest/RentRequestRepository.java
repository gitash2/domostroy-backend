package domostroy.core.application.rentRequest;

import domostroy.aggregates.rentRequest.RentRequestStatus;
import domostroy.core.adapters.adaptersOutput.rentRequest.projections.RentRequestProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RentRequestRepository {
    RentRequestProjection save(RentRequestProjection rentRequestProjection);
    Page<RentRequestProjection> findAllIncomingRequests(List<Long> ids, Pageable pageable);
    Page<RentRequestProjection> findAllOutgoingRequests(Long userId, Pageable pageable);

    void changeRequestStatus(Long requestId, RentRequestStatus status);
    RentRequestProjection findById(Long id);
    void deleteAllByOfferId(Long offerId);
}
