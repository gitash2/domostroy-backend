package domostroy.core.adapters.adaptersInput.dto.mappers;

import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.CreateOfferRequest;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.CreateOfferResponse;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.OfferDTO;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.UpdateOfferDTO;
import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OfferMapper {
    OfferDTO toDTO(OfferProjection offer);

    OfferProjection toJPAEntity(CreateOfferRequest dto);
}
