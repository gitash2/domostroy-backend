package domostroy.core.adapters.adaptersInput.dto.input.mobile.offers;

import domostroy.aggregates.currency.Currency;

import java.time.LocalDate;
import java.util.List;

public record CreateOfferRequest(
    String title,
    String description,
    Integer categoryId,
    Currency currency,
    Double price,
    Integer cityId,
    List<LocalDate> rentDates
) {
}
