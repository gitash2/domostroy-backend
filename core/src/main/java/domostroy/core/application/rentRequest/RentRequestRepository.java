package domostroy.core.application.rentRequest;

import domostroy.aggregates.rentRequest.RentRequestStatus;
import domostroy.core.adapters.adaptersOutput.rentRequest.projections.RentRequestProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface RentRequestRepository {
    RentRequestProjection save(RentRequestProjection rentRequestProjection);
    Page<RentRequestProjection> findAllIncomingRequests(List<Long> ids, Pageable pageable);
    Page<RentRequestProjection> findAllOutgoingRequests(Long userId, Pageable pageable);

    void changeRequestStatus(Long requestId, RentRequestStatus status);
    RentRequestProjection findById(Long id);
    void deleteAllByOfferId(Long offerId);
    List<RentRequestProjection> findAllByOfferIdAndDatesIn(Long offerId, List<LocalDate> dates);
    void deleteAll(List<RentRequestProjection> requests);
    void flush();
    void deleteByRequestId(Long requestId);
    void saveAll(List<RentRequestProjection> requests);
    boolean isMyRentRequest(Long requestId, Long userId);
}
