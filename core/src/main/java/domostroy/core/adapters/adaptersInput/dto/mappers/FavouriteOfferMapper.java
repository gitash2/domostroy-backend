package domostroy.core.adapters.adaptersInput.dto.mappers;

import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.preview.FavouriteOfferDTO;
import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import domostroy.core.application.cloudStorage.FileStorageService;
import domostroy.core.application.cloudStorage.OfferPhotoRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavouriteOfferMapper {

    @Mapping(target = "photoUrl", expression = "java(getPhotoUrl(offer, photoRepository, fileStorageService))")
    FavouriteOfferDTO toDTO(
            OfferProjection offer,
            @Context OfferPhotoRepository photoRepository,
            @Context FileStorageService fileStorageService
    );



    default String getPhotoUrl(OfferProjection offer,
                               OfferPhotoRepository photoRepository,
                               FileStorageService fileStorageService
    ) {
        String firstPath = photoRepository.findAllPhotoPathsByOfferId(offer.getId()).getFirst();
        return fileStorageService.getPresignedUrl(firstPath);

    }
}
