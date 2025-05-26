package domostroy.core.adapters.adaptersInput.dto.input.mobile.offers;

import domostroy.aggregates.currency.Currency;

import java.util.List;

public record UpdateOfferDTO(
    Long id,
    String title,
    String description,
    Integer categoryId,
    Currency currency,
    Double price,
    Integer cityId,
    List<Long> photoIds
) {
}
