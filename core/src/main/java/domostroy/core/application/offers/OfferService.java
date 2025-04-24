package domostroy.core.application.offers;

import domostroy.aggregates.offer.domain.OfferAggregate;
import domostroy.aggregates.offer.domain.OfferPhoto;
import domostroy.core.adapters.adaptersInput.dto.input.offers.CreateOfferRequest;
import domostroy.core.adapters.adaptersInput.dto.input.offers.CreateOfferResponse;
import domostroy.core.adapters.adaptersInput.dto.input.offers.OfferDTO;
import domostroy.core.adapters.adaptersOutput.offerCalendar.projections.OfferCalendarProjection;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import domostroy.core.application.cloudStorage.FileStorageService;
import domostroy.core.application.cloudStorage.OfferPhotoRepository;
import domostroy.core.application.cloudStorage.config.YandexCloudProperties;
import domostroy.core.application.offerCalendar.OfferCalendarRepository;
import domostroy.core.application.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final OfferPhotoRepository offerPhotoRepository;
    private final YandexCloudProperties yandexCloudProperties;
    private final OfferCalendarRepository offerCalendarRepository;
    private final String OFFERS_PATH = "offerPhotos";

    @Transactional
    public CreateOfferResponse createOffer(CreateOfferRequest dto, Collection<MultipartFile> files, UserDetails user) {
        User client = userRepository.findByEmail(user.getUsername());
        OfferAggregate offer = new OfferAggregate(
                null,
                dto.title(),
                dto.description(),
                dto.category(),
                dto.currency(),
                dto.price(),
                dto.cityId(),
                client.getId(),
                LocalDateTime.now()
        );
        OfferAggregate savedOffer = offerRepository.save(offer);

        List<String> paths = files.stream()
                .map(file -> constructFileStoragePath(savedOffer))
                .collect(Collectors.toList());
        Collection<InputStream> photos = files.stream()
                .map(file -> {
                    try {
                        return file.getInputStream();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .collect(Collectors.toList());


        List<OfferPhoto> offerPhotos = paths.stream()
                .map(path -> new OfferPhoto(
                        null,
                        savedOffer.getOfferId(),
                        yandexCloudProperties.getBucket(),
                        path,
                        LocalDateTime.now()
                )).toList();

        List<OfferCalendarProjection> dates = dto.rentDates().stream()
                .map(it -> new OfferCalendarProjection(
                        null,
                        it,
                        savedOffer.getOfferId(),
                        false
                )).toList();
        offerCalendarRepository.saveOfferDates(dates);
        offerPhotoRepository.saveAll(offerPhotos);

        try {
            fileStorageService.saveAllFiles(photos, paths);
        } catch (UncheckedIOException e) {
            throw new RuntimeException(e);
        }

        return new CreateOfferResponse(savedOffer);
    }

    public OfferDTO getOfferData(Long offerId) {
        Collection<OfferPhoto> photos = offerPhotoRepository.findAllByOfferId(offerId);
        Collection<String> photoUrls = photos.stream()
                .map(it -> fileStorageService.getPresignedUrl(it.imagePath()))
                .toList();
        OfferAggregate offer = offerRepository.getOfferById(offerId);
        return new OfferDTO(offer, photoUrls);
    }


    public void deleteOffer(Long offerId) {
        List<String> paths = offerPhotoRepository.findAllPhotoPathsByOfferId(offerId);
        fileStorageService.deleteFiles(paths);
        offerCalendarRepository.deleteAllByOfferId(offerId);
        offerRepository.deleteOffer(offerId);
    }

    private String constructFileStoragePath(OfferAggregate aggregate) {
        return String.format("%s/%s/%s", OFFERS_PATH, aggregate.getOfferId(), UUID.randomUUID());
    }
}
