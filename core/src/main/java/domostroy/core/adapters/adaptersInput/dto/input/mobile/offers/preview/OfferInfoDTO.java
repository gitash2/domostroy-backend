package domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.preview;

import domostroy.aggregates.currency.Currency;


public record OfferInfoDTO(
        Long id,
        String title,
        Double price,
        Currency currency,
        String photoUrl,
        String city,
        Boolean isFavourite,
        boolean isBanned,
        String banReason
) {
}
