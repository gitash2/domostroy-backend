package domostroy.core.adapters.adaptersInput.dto.output.rentRequest;

import domostroy.aggregates.rentRequest.RentRequestStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record RentRequestDTO(
        Long id,
        RentRequestStatus status,
        Set<LocalDate> dates,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt,
        RentOfferDTO offer,
        RentUserDTO user
) {
}
