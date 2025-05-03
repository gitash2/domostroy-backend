package domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.preview;

import domostroy.aggregates.currency.Currency;

public record FavouriteOfferDTO(
        Long id,
        String title,
        Double price,
        Currency currency,
        String photoUrl,
        Long lessorId
) {
}
