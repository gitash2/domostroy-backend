package domostroy.core.adapters.adaptersInput.dto.input.offers;

import java.time.LocalDate;
import java.util.List;

public record CreateOfferRequest(
    String title,
    String description,
    String category,
    String currency,
    Double price,
    Integer cityId,
    List<LocalDate> rentDates
) {
}
