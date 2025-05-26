package domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.preview;

import domostroy.aggregates.currency.Currency;

import java.time.LocalDate;

public record MyOfferDTO(
        Long id,
        String title,
        String description,
        Double price,
        Currency currency,
        String photoUrl,
        LocalDate createdAt,
        boolean isBanned,
        String banReason
) {
}
