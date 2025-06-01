package domostroy.core.application.offers;

import domostroy.core.adapters.adaptersInput.dto.input.misc.PaginationOutput;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.*;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.admin.AdminOfferInfoDTO;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.admin.AdminOfferOutput;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.admin.BanOfferDTO;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.preview.FavouriteOfferDTO;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.preview.MyOfferDTO;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.preview.OfferInfoDTO;
import domostroy.core.adapters.adaptersInput.dto.output.calendar.CalendarDTO;
import domostroy.core.adapters.adaptersInput.dto.output.calendar.CalendarOutput;
import domostroy.core.adapters.adaptersInput.dto.output.offers.ModerationResponse;
import domostroy.core.adapters.adaptersInput.dto.output.offers.OfferOutput;
import domostroy.core.adapters.adaptersOutput.offerCalendar.projections.OfferCalendarProjection;
import domostroy.core.adapters.adaptersOutput.offerPhotos.projections.OfferPhotoProjection;
import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import domostroy.core.application.cities.CityRepository;
import domostroy.core.application.cloudStorage.FileStorageService;
import domostroy.core.application.cloudStorage.OfferPhotoRepository;
import domostroy.core.application.cloudStorage.config.YandexCloudProperties;
import domostroy.core.application.deepseek.LLMService;
import domostroy.core.application.deepseek.ModerationService;
import domostroy.core.application.misc.filter.FilterSpecificationBuilder;
import domostroy.core.application.misc.filter.SearchDTO;
import domostroy.core.application.offerCalendar.OfferCalendarRepository;
import domostroy.core.application.rentRequest.RentRequestRepository;
import domostroy.core.application.users.UserRepository;
import domostroy.core.exceptions.ModerationException;
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
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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
    private final RentRequestRepository rentRequestRepository;
    private final ModerationService moderationService;
    private final LLMService llmService;
    private final String OFFERS_PATH = "offerPhotos";

    @Transactional
    public CreateOfferResponse createOffer(CreateOfferRequest dto, Collection<MultipartFile> files, UserDetails user) {
        User user2 = userRepository.findByEmail(user.getUsername());

        User client = userRepository.findByEmail(user.getUsername());
        OfferProjection offer = OfferProjection.builder()
                .title(dto.title())
                .description(dto.description())
                .price(dto.price())
                .categoryId(dto.categoryId())
                .currency(dto.currency())
                .cityId(dto.cityId())
                .userId(client.getId())
                .createdAt(LocalDateTime.now())
                .isBanned(false)
                .banReason(null)
                .user(user2)
                .build();

        OfferProjection savedOffer = offerRepository.save(offer);

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

        List<OfferPhotoProjection> offerPhotos = paths.stream()
                .map(path -> new OfferPhotoProjection(
                        null,
                        savedOffer.getId(),
                        yandexCloudProperties.getBucket(),
                        path,
                        LocalDateTime.now()
                )).toList();

        List<OfferCalendarProjection> dates = dto.rentDates().stream()
                .map(it -> new OfferCalendarProjection(
                        null,
                        it,
                        savedOffer.getId(),
                        false,
                        null
                )).toList();
        offerCalendarRepository.saveOfferDates(dates);
        offerPhotoRepository.saveAll(offerPhotos);

        try {
            fileStorageService.saveAllFiles(photos, paths);
        } catch (UncheckedIOException e) {
            throw new RuntimeException(e);
        }

        CreateOfferResponse response = new CreateOfferResponse(savedOffer);
        moderationService.sendForModeration(savedOffer.getId(), dto.title(), dto.description());
        return response;
    }

    @Transactional
    public void update(UpdateOfferDTO dto, Collection<MultipartFile> files, UserDetails user) {
        OfferProjection offer = offerRepository.findById(dto.id());

        offer.setCategoryId(dto.categoryId());
        offer.setCurrency(dto.currency());
        offer.setPrice(dto.price());
        offer.setDescription(dto.description());
        offer.setTitle(dto.title());
        offer.setCityId(dto.cityId());

        List<String> photoPathsNotInList = offerPhotoRepository.findAllPhotosIdsNotInList(dto.photoIds(), offer.getId());
        offerPhotoRepository.deleteAllPhotosNotInList(dto.photoIds(), offer.getId());

        OfferProjection savedOffer = offerRepository.save(offer);

        if (files == null) {
            fileStorageService.deleteFiles(photoPathsNotInList);
            return;
        }
        List<String> paths = files.stream()
                .map(file -> constructFileStoragePath(savedOffer))
                .toList();
        Collection<InputStream> photos = files.stream()
                .map(file -> {
                    try {
                        return file.getInputStream();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .toList();

        List<OfferPhotoProjection> offerPhotos = paths.stream()
                .map(path -> new OfferPhotoProjection(
                        null,
                        savedOffer.getId(),
                        yandexCloudProperties.getBucket(),
                        path,
                        LocalDateTime.now()
                )).toList();

        offerPhotoRepository.saveAll(offerPhotos);

        fileStorageService.saveAllFiles(photos, paths);
        fileStorageService.deleteFiles(photoPathsNotInList);

        offerRepository.save(offer);
        moderationService.sendForModeration(offer.getId(), offer.getTitle(), offer.getDescription());
    }

    public OfferDTO getOfferData(UserDetails user, Long offerId) {
        boolean isFavourite = false;
        if (user != null) {
            isFavourite = offerRepository.isFavourite(offerId, user.getUsername());
        }

        Collection<OfferPhotoProjection> photos = offerPhotoRepository.findAllByOfferId(offerId);
        List<OfferPhoto> photoUrls = photos.stream()
                .map(it -> new OfferPhoto(it.getId(),
                        fileStorageService.getPresignedUrl(it.getImagePath())))
                .toList();


        OfferProjection offer = offerRepository.getOfferById(offerId);

        return new OfferDTO(offer, photoUrls, isFavourite);
    }

    @Transactional
    public void banOffer(BanOfferDTO dto) {
        OfferProjection offer = offerRepository.findById(dto.offerId());
        offer.setBanned(true);
        offer.setBanReason(dto.banReason());
        offerRepository.save(offer);
    }

    @Transactional
    public void unbanOffer(Long offerId) {
        OfferProjection offer = offerRepository.findById(offerId);
        offer.setBanReason(null);
        offer.setBanned(false);
    }

    @PreAuthorize("hasAuthority('ADMIN') or @offerService.isOwner(#offerId, principal.username)")
    @Transactional
    public void deleteOffer(Long offerId) {
        List<String> paths = offerPhotoRepository.findAllPhotoPathsByOfferId(offerId);
        offerPhotoRepository.deleteAllPhotosByOfferId(offerId);
        offerCalendarRepository.deleteAllByOfferId(offerId);
        rentRequestRepository.deleteAllByOfferId(offerId);
        offerRepository.deleteOffer(offerId);
        fileStorageService.deleteFiles(paths);
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
                            proj.getCreatedAt().toLocalDate(),
                            proj.isBanned(),
                            proj.getBanReason()
                    );
                });
    }

    private String constructFileStoragePath(OfferProjection offer) {
        return String.format("%s/%s/%s", OFFERS_PATH, offer.getId(), UUID.randomUUID());
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
    public void addOfferToFavourites(Long offerId, String email, boolean isFavourite) {
        User user = userRepository.findByEmail(email);
        OfferProjection offer = offerRepository.getOfferById(offerId);
        if (offer.getUserId().equals(user.getId())) {
            return;
        }
        Set<OfferProjection> favourites = user.getFavourites();
        if (isFavourite) {
            favourites.add(offer);
        } else {
            favourites.remove(offer);
        }
        userRepository.save(user);
    }

    /*public OfferOutput search(UserDetails user, SearchDTO dto) {
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
    }*/

    public OfferOutput search(UserDetails user, SearchDTO dto) {
        String username = user != null ? user.getUsername() : null;
        User user2 = userRepository.findByEmail(username);
        Page<OfferProjection> offers = getOffersPage(dto, user2);
        Page<OfferInfoDTO> data = mapToOfferInfoPage(username, offers);
        return new OfferOutput(PaginationOutput.fromPage(offers), data);
    }

    private Page<OfferProjection> getOffersPage(SearchDTO dto, UserDetails user) {
        FilterSpecificationBuilder<OfferProjection> builder =
                new FilterSpecificationBuilder<OfferProjection>()
                        .withCriteriaFrom(dto.searchCriteriaList());

        boolean isAdmin = user != null && user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));

        if (!isAdmin) {
            builder.with("isBanned", "eq", false)
                    .with("user.isBanned", "eq", false);
        }


        if (dto.pas().seed() != null && dto.pas().snapshot() != null) {
            builder.withRandomOrder(dto.pas().seed(), dto.pas().snapshot());
        }

        Specification<OfferProjection> spec = builder.build();
        Pageable pageReq = dto.pas().toPageRequest();
        return offerRepository.findAll(spec, pageReq);
    }

    public AdminOfferOutput searchAdmin(SearchDTO dto, UserDetails user) {
        Page<OfferProjection> offers = getOffersPage(dto, user);
        Page<AdminOfferInfoDTO> data = mapToAdminOfferInfoPage(user.getUsername(), offers);
        return new AdminOfferOutput(PaginationOutput.fromPage(offers), data);
    }

    private Page<AdminOfferInfoDTO> mapToAdminOfferInfoPage(String email, Page<OfferProjection> page) {
        List<AdminOfferInfoDTO> dtos = page.getContent().stream()
                .filter(proj -> email == null || !offerRepository.isMyOffer(proj.getId(), email))
                .map(proj -> {
                    String firstPath = offerPhotoRepository.findAllPhotoPathsByOfferId(proj.getId())
                            .stream().findFirst()
                            .orElseThrow(() -> new ObjectNotFoundException("No photo for offer " + proj.getId()));
                    return new AdminOfferInfoDTO(
                            proj.getId(),
                            proj.getTitle(),
                            proj.getDescription(),
                            proj.getPrice(),
                            proj.getCurrency(),
                            fileStorageService.getPresignedUrl(firstPath),
                            cityRepository.findById(proj.getCityId()).getName(),
                            proj.isBanned(),
                            proj.getBanReason()
                    );
                }).toList();
        return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
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
                            cityRepository.findById(proj.getCityId()).getName(),
                            isFav,
                            proj.isBanned(),
                            proj.getBanReason()
                    );
                }).toList();

        return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
    }

    public CalendarOutput getCalendar(Long offerId) {
        return new CalendarOutput(offerCalendarRepository.findOfferDates(offerId).stream().map(
                it -> new CalendarDTO(
                        it.getDate(),
                        it.isBooked()
                )
        ).collect(Collectors.toList()));
    }

    @Transactional
    public void updateAvailableDates(UpdateAvailableDatesDTO dto) {
        offerCalendarRepository.deleteUnavailableDates(dto.availableDates(), dto.offerId());
    }
}
