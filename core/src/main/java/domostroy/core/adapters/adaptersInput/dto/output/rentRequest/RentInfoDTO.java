package domostroy.core.adapters.adaptersInput.dto.output.rentRequest;

import domostroy.aggregates.currency.Currency;
import domostroy.aggregates.rentRequest.RentRequestStatus;

import java.time.LocalDate;
import java.util.Set;

public record RentInfoDTO(
        Long id,
        String title,
        Double price,
        Currency currency,
        RentRequestStatus status,
        Set<LocalDate> dates,
        LocalDate createdAt,
        LocalDate resolvedAt,
        String firstName,
        String phoneNumber,
        String photoUrl

) {
}
