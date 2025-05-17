package domostroy.core.adapters.adaptersInput.dto.output.rentRequest;

import domostroy.aggregates.currency.Currency;

public record RentOfferDTO(
        Long offerId,
        String title,
        String photoUrl,
        Double price,
        Currency currency,
        String city
) {
}
