package domostroy.core.adapters.adaptersInput.dto.output.rentRequest;

import domostroy.aggregates.currency.Currency;
import domostroy.aggregates.rentRequest.RentRequestStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record RentInfoDTO(
        Long id,
        Long offerId,
        Long userId,
        String city,
        String title,
        Double price,
        Currency currency,
        RentRequestStatus status,
        Set<LocalDate> dates,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt,
        String name,
        String phoneNumber,
        String photoUrl

) {
}
