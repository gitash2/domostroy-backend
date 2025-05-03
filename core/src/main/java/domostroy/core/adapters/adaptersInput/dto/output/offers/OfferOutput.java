package domostroy.core.adapters.adaptersInput.dto.output.offers;

import domostroy.core.adapters.adaptersInput.dto.input.misc.PaginationOutput;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.preview.OfferInfoDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public record OfferOutput(
        PaginationOutput pagination,
        Page<OfferInfoDTO> data
) {
}
