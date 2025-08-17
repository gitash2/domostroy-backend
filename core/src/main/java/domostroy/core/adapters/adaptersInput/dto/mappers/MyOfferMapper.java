package domostroy.core.adapters.adaptersInput.dto.mappers;

import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.preview.MyOfferDTO;
import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import domostroy.core.application.cloudStorage.FileStorageService;
import domostroy.core.application.cloudStorage.OfferPhotoRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MyOfferMapper {

    @Mapping(target = "photoUrl", expression = "java(getPhotoUrl(proj, photoRepository, fileStorageService))")
    @Mapping(target = "createdAt", expression = "java(proj.getCreatedAt().toLocalDate())")
    MyOfferDTO toDTO(
            OfferProjection proj,
            @Context OfferPhotoRepository photoRepository,
            @Context FileStorageService fileStorageService
    );

    default String getPhotoUrl(OfferProjection proj,
                               OfferPhotoRepository photoRepository,
                               FileStorageService fileStorageService
    ) {
        String firstPath = photoRepository.findAllPhotoPathsByOfferId(proj.getId()).getFirst();
        return fileStorageService.getPresignedUrl(firstPath);

    }
}
