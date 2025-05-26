package domostroy.core.adapters.adaptersOutput.rentRequest.dao;

import domostroy.aggregates.rentRequest.RentRequestStatus;
import domostroy.core.adapters.adaptersOutput.offerCalendar.projections.OfferCalendarProjection;
import domostroy.core.adapters.adaptersOutput.rentRequest.projections.RentRequestProjection;
import domostroy.core.application.rentRequest.RentRequestRepository;
import domostroy.core.exceptions.ObjectNotFoundException;
import domostroy.core.exceptions.rentRequest.InvalidStatusChangeException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RentRequestJPARepository implements RentRequestRepository {
    private final RentRequestDAO rentRequestDAO;


    @Override
    public RentRequestProjection save(RentRequestProjection rentRequestProjection) {
        return rentRequestDAO.save(rentRequestProjection);
    }

    @Override
    public Page<RentRequestProjection> findAllIncomingRequests(List<Long> ids, Pageable pageable) {
        return rentRequestDAO.findAllIncomingRequests(ids, pageable);
    }

    @Override
    public Page<RentRequestProjection> findAllOutgoingRequests(Long userId, Pageable pageable) {
        return rentRequestDAO.findAllOutgoingRequests(userId, pageable);
    }

    @Override
    public void changeRequestStatus(Long requestId, RentRequestStatus status) {
        RentRequestProjection request = rentRequestDAO
                .findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("RentRequest with id " + requestId + " not found"));

        if (request.getStatus().toString().equals(RentRequestStatus.PENDING.toString())) {
            request.setStatus(status);
            rentRequestDAO.save(request);
        } else {
            throw new InvalidStatusChangeException("RentRequest with id " + requestId + " is already " + request.getStatus().toString().toLowerCase());
        }

    }

    @Override
    public RentRequestProjection findById(Long id) {
        return rentRequestDAO
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException
                        ("RentRequest with id " + id + " not found"));
    }

    @Override
    public void deleteAllByOfferId(Long offerId) {
        rentRequestDAO.deleteAllByOfferId(offerId);
    }

    @Override
    public List<RentRequestProjection> findAllByOfferIdAndDatesIn(Long offerId, List<LocalDate> dates) {
        return rentRequestDAO.findAllByOfferIdAndDatesIn(offerId, dates);
    }

    @Override
    public void deleteAll(List<RentRequestProjection> requests) {
        rentRequestDAO.deleteAll(requests);
    }

    @Override
    public void flush() {
        rentRequestDAO.flush();
    }

    @Override
    @Transactional
    public void deleteByRequestId(Long requestId) {
        rentRequestDAO.deleteById(requestId);
        rentRequestDAO.deleteRentRequestDatesByRentRequestId(requestId);
    }

    @Override
    public void saveAll(List<RentRequestProjection> requests) {
        rentRequestDAO.saveAll(requests);
    }

    @Override
    public boolean isMyRentRequest(Long requestId, Long userId) {
        return rentRequestDAO.isMyRentRequest(requestId, userId);
    }


}
