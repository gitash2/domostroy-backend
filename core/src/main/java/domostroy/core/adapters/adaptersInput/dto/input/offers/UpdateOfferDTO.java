package domostroy.core.adapters.adaptersInput.dto.input.offers;

import domostroy.aggregates.currency.Currency;

public record UpdateOfferDTO(
    Long id,
    String title,
    String description,
    String category,
    Currency currency,
    Double price,
    Integer cityId
) {
}
