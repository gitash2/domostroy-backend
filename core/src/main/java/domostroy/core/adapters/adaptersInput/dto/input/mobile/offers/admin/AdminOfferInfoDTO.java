package domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.admin;

import domostroy.aggregates.currency.Currency;

public record AdminOfferInfoDTO(
        Long id,
        String title,
        String description,
        Double price,
        Currency currency,
        String photoUrl,
        String city,
        Boolean isBanned,
        String banReason
) {
}
