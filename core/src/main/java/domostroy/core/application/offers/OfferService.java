package domostroy.core.application.offers;

import domostroy.aggregates.offer.domain.OfferAggregate;
import domostroy.aggregates.offer.domain.OfferPhoto;
import domostroy.core.adapters.adaptersInput.dto.input.misc.PaginationOutput;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.CreateOfferRequest;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.CreateOfferResponse;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.OfferDTO;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.preview.FavouriteOfferDTO;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.preview.MyOfferDTO;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.preview.OfferInfoDTO;
import domostroy.core.adapters.adaptersInput.dto.output.offers.OfferOutput;
import domostroy.core.adapters.adaptersOutput.offerCalendar.projections.OfferCalendarProjection;
import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import domostroy.core.application.cities.CityRepository;
import domostroy.core.application.cloudStorage.FileStorageService;
import domostroy.core.application.cloudStorage.OfferPhotoRepository;
import domostroy.core.application.cloudStorage.config.YandexCloudProperties;
import domostroy.core.application.misc.filter.FilterSpecificationBuilder;
import domostroy.core.application.misc.filter.SearchDTO;
import domostroy.core.application.offerCalendar.OfferCalendarRepository;
import domostroy.core.application.users.UserRepository;
import domostroy.core.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.Instant;
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
    private final CityRepository cityRepository;
    private final String OFFERS_PATH = "offerPhotos";

    @Transactional
    public CreateOfferResponse createOffer(CreateOfferRequest dto, Collection<MultipartFile> files, UserDetails user) {
        User client = userRepository.findByEmail(user.getUsername());
        OfferAggregate offer = new OfferAggregate(
                null,
                dto.title(),
                dto.description(),
                dto.categoryId(),
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

    public OfferDTO getOfferData(UserDetails user, Long offerId) {
        boolean isFavourite = false;
        if (user != null) {
            isFavourite = offerRepository.isFavourite(offerId, user.getUsername());
        }

        Collection<OfferPhoto> photos = offerPhotoRepository.findAllByOfferId(offerId);
        Collection<String> photoUrls = photos.stream()
                .map(it -> fileStorageService.getPresignedUrl(it.imagePath()))
                .toList();
        OfferProjection offer = offerRepository.getOfferById(offerId);

        return new OfferDTO(offer, photoUrls, isFavourite);
    }


    @PreAuthorize("hasAuthority('ADMIN') or @offerService.isOwner(#offerId, principal.username)")
    @Transactional
    public void deleteOffer(Long offerId) {
        List<String> paths = offerPhotoRepository.findAllPhotoPathsByOfferId(offerId);
        fileStorageService.deleteFiles(paths);
        offerCalendarRepository.deleteAllByOfferId(offerId);
        offerRepository.deleteOffer(offerId);
    }


    public boolean isOwner(Long offerId, String username) {
        return offerRepository.isMyOffer(offerId, username);
    }

    public Page<MyOfferDTO> getMyOffers(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email);
        return offerRepository.getMyOffers(user.getId(), pageable)
                .map(proj -> {
                    String firstPath = offerPhotoRepository
                            .findAllPhotoPathsByOfferId(proj.getId())
                            .getFirst();
                    return new MyOfferDTO(
                            proj.getId(),
                            proj.getTitle(),
                            proj.getDescription(),
                            proj.getPrice(),
                            proj.getCurrency(),
                            fileStorageService.getPresignedUrl(firstPath),
                            proj.getCreatedAt().toLocalDate()
                    );
                });
    }


    private String constructFileStoragePath(OfferAggregate aggregate) {
        return String.format("%s/%s/%s", OFFERS_PATH, aggregate.getOfferId(), UUID.randomUUID());
    }

    @Transactional
    public Page<FavouriteOfferDTO> getFavouriteOffers(Pageable pageable, String email) {
        User user = userRepository.findByEmail(email);
        Page<OfferProjection> favouriteOffers = offerRepository.findFavouriteOffersByUserId(user.getId(), pageable);

        return favouriteOffers
                .map(it -> new FavouriteOfferDTO(
                        it.getId(),
                        it.getTitle(),
                        it.getDescription(),
                        it.getPrice(),
                        it.getCurrency(),
                        fileStorageService.getPresignedUrl(offerPhotoRepository.findAllPhotoPathsByOfferId(it.getId()).getFirst()),
                        it.getUserId()
                ));
    }

    @Transactional
    public void addOfferToFavourites(Long offerId, String email) {
        User user = userRepository.findByEmail(email);
        OfferProjection offer = offerRepository.getOfferById(offerId);
        if (offer.getUserId().equals(user.getId())) {
            return;
        }
        if (offerRepository.isFavourite(offerId, email)) {
            user.getFavourites().remove(offer);
        } else {
            user.getFavourites().add(offer);
        }
        userRepository.save(user);
    }

    public OfferOutput search(UserDetails user, SearchDTO dto) {
        String username = user != null ? user.getUsername() : null;

        FilterSpecificationBuilder<OfferProjection> builder =
                new FilterSpecificationBuilder<OfferProjection>()
                        .withCriteriaFrom(dto.searchCriteriaList());

        if (dto.pas().seed() != null && dto.pas().snapshot() != null) {
            builder.withRandomOrder(dto.pas().seed(), dto.pas().snapshot());
        }

        Specification<OfferProjection> spec = builder.build();

        Pageable pageReq = dto.pas().toPageRequest();

        Page<OfferProjection> offers =
                offerRepository.findAll(spec, pageReq);

        Page<OfferInfoDTO> data = mapToOfferInfoPage(username, offers);
        return new OfferOutput(PaginationOutput.fromPage(offers), data);
    }

    private Page<OfferInfoDTO> mapToOfferInfoPage(String email, Page<OfferProjection> page) {
        List<OfferInfoDTO> dtos = page.getContent().stream()
                .filter(proj -> email == null || !offerRepository.isMyOffer(proj.getId(), email))
                .map(proj -> {
                    boolean isFav = email != null && offerRepository.isFavourite(proj.getId(), email);
                    String firstPath = offerPhotoRepository.findAllPhotoPathsByOfferId(proj.getId())
                            .stream().findFirst()
                            .orElseThrow(() -> new ObjectNotFoundException("No photo for offer " + proj.getId()));
                    return new OfferInfoDTO(
                            proj.getId(),
                            proj.getTitle(),
                            proj.getPrice(),
                            proj.getCurrency(),
                            fileStorageService.getPresignedUrl(firstPath),
                            cityRepository.findById(proj.getCityId()),
                            isFav
                    );
                }).toList();

        return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
    }
}
