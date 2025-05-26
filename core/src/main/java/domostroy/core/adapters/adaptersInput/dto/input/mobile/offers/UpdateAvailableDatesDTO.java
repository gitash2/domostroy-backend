package domostroy.core.adapters.adaptersInput.dto.input.mobile.offers;

import java.time.LocalDate;
import java.util.List;

public record UpdateAvailableDatesDTO(
        Long offerId,
        List<LocalDate> availableDates
) {
}
