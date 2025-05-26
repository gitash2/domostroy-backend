package domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.admin;

import domostroy.core.adapters.adaptersInput.dto.input.misc.PaginationOutput;
import org.springframework.data.domain.Page;

public record AdminOfferOutput(
        PaginationOutput pagination,
        Page<AdminOfferInfoDTO> data
) {
}
