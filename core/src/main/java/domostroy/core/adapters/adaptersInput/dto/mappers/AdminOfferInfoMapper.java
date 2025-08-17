package domostroy.core.adapters.adaptersInput.dto.mappers;

import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.admin.AdminOfferInfoDTO;
import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import domostroy.core.application.cities.CityRepository;
import domostroy.core.application.cloudStorage.FileStorageService;
import domostroy.core.application.cloudStorage.OfferPhotoRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminOfferInfoMapper {

    @Mapping(target = "photoUrl", expression = "java(getPhotoUrl(proj, photoRepository, fileStorageService))")
    @Mapping(target = "city", expression = "java(getCityName(proj, cityRepository))")
    AdminOfferInfoDTO toDTO(
            OfferProjection proj,
            @Context OfferPhotoRepository photoRepository,
            @Context FileStorageService fileStorageService,
            @Context CityRepository cityRepository
    );

    default String getPhotoUrl(OfferProjection proj,
                               OfferPhotoRepository photoRepository,
                               FileStorageService fileStorageService
    ) {
        String firstPath = photoRepository.findAllPhotoPathsByOfferId(proj.getId()).getFirst();
        return fileStorageService.getPresignedUrl(firstPath);
    }

    default String getCityName(OfferProjection proj,CityRepository cityRepository) {
        return cityRepository.findById(proj.getCityId()).getName();
    }
}
