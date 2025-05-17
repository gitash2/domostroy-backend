package domostroy.core.adapters.adaptersInput.dto.input.mobile.rent;

import java.time.LocalDate;
import java.util.Set;

public record CreateRentRequestDTO(
        Long offerId,
        Set<LocalDate> dates
) {
}
