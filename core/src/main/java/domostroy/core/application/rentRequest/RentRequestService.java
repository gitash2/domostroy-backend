package domostroy.core.application.rentRequest;

import domostroy.aggregates.offer.domain.OfferPhotoPath;
import domostroy.aggregates.rentRequest.RentRequestStatus;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.rent.CreateRentRequestDTO;
import domostroy.core.adapters.adaptersInput.dto.output.rentRequest.*;
import domostroy.core.adapters.adaptersOutput.cities.projections.CityProjection;
import domostroy.core.adapters.adaptersOutput.offerCalendar.projections.OfferCalendarProjection;
import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import domostroy.core.adapters.adaptersOutput.rentRequest.projections.RentRequestProjection;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import domostroy.core.application.cities.CityRepository;
import domostroy.core.application.cloudStorage.FileStorageService;
import domostroy.core.application.cloudStorage.OfferPhotoRepository;
import domostroy.core.application.offerCalendar.OfferCalendarRepository;
import domostroy.core.application.offers.OfferRepository;
import domostroy.core.application.users.UserRepository;
import domostroy.core.exceptions.rentRequest.InvalidRentRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;


import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class RentRequestService {

    private final RentRequestRepository rentRequestRepository;
    private final OfferCalendarRepository offerCalendarRepository;
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final CityRepository cityRepository;
    private final FileStorageService fileStorageService;
    private final OfferPhotoRepository offerPhotoRepository;

    @Transactional
    public void createRentRequest(CreateRentRequestDTO dto, UserDetails user) {
        Long offerId = dto.offerId();
        Set<LocalDate> dates = dto.dates();

        if (areDatesInvalid(dates, offerId)) {
            throw new InvalidRentRequestException("Some dates are booked or unavailable");
        }
        User userProjection = userRepository.findByEmail(user.getUsername());
        Set<OfferCalendarProjection> datesProj = offerCalendarRepository.getOfferDates(offerId, dates);

        RentRequestProjection rentRequestProjection = new RentRequestProjection(
                null,
                offerId,
                userProjection.getId(),
                LocalDate.now(),
                null,
                RentRequestStatus.PENDING,
                datesProj
        );
        rentRequestRepository.save(rentRequestProjection);
    }

    @Transactional
    public void changeRequestStatus(Long requestId, RentRequestStatus status) {
        rentRequestRepository.changeRequestStatus(requestId, status);
        RentRequestProjection rentRequest = rentRequestRepository.findById(requestId);
        rentRequest.getDates().forEach(date -> date.setBooked(true));
        rentRequest.setResolvedAt(LocalDate.now());
    }

    private boolean areDatesInvalid(Set<LocalDate> date, Long offerId) {
        return offerCalendarRepository.areDatesBooked(date, offerId);
    }

    public Page<RentRequestDTO> getIncomingRequests(UserDetails user, Pageable pageable) {
        User me = userRepository.findByEmail(user.getUsername());
        List<OfferProjection> offers = offerRepository.getMyOffersIds(me.getId());
        List<Long> offerIds = offers.stream().map(OfferProjection::getId).toList();
        Page<RentRequestProjection> requests =
                rentRequestRepository.findAllIncomingRequests(offerIds, pageable);

        return buildRequestsOutput(requests, offers);
    }

    public Page<RentRequestDTO> getOutgoingRequests(UserDetails user, Pageable pageable) {
        User me = userRepository.findByEmail(user.getUsername());
        Page<RentRequestProjection> requests =
                rentRequestRepository.findAllOutgoingRequests(me.getId(), pageable);

        List<OfferProjection> offers =
                offerRepository.findAllByIds(
                        requests.stream().map(RentRequestProjection::getOfferId).toList()
                );

        return buildRequestsOutput(requests, offers);
    }

    public RentInfoDTO getRequestInfo(Long requestId, boolean incoming) {
        RentRequestProjection req = rentRequestRepository.findById(requestId);

        OfferProjection offer = offerRepository.getOfferById(req.getOfferId());
        User targetUser = incoming
                ? userRepository.findById(req.getUserId())
                : userRepository.findById(offer.getUserId());

        String photoPath = offerPhotoRepository
                .findFirstPhotoPathByOfferId(offer.getId());

        return new RentInfoDTO(
                requestId,
                offer.getTitle(), offer.getPrice(), offer.getCurrency(),
                req.getStatus(),
                req.getDates().stream().map(OfferCalendarProjection::getDate).collect(toSet()),
                req.getCreatedAt(), req.getResolvedAt(),
                targetUser.getFirstName(),
                targetUser.getPhoneNumber(),
                fileStorageService.getPresignedUrl(photoPath)
        );
    }

    private Page<RentRequestDTO> buildRequestsOutput(
            Page<RentRequestProjection> requests,
            List<OfferProjection> offers
    ) {
        List<Long> userIds = requests.stream()
                .map(RentRequestProjection::getUserId)
                .collect(toList());
        List<Long> offerIds = offers.stream()
                .map(OfferProjection::getId)
                .collect(toList());
        List<Integer> cityIds = offers.stream()
                .map(OfferProjection::getCityId)
                .collect(toList());

        Map<Long, User> userById = userRepository.findAllByIds(userIds)
                .stream()
                .collect(toMap(User::getId, identity()));
        Map<Integer, String> cityById = cityRepository.findAllByIds(cityIds)
                .stream()
                .collect(toMap(
                        CityProjection::getId,
                        CityProjection::getName));
        Map<Long, String> photoByOffer = offerPhotoRepository
                .findFirstPhotoPathByOfferId(offerIds)
                .stream()
                .collect(toMap(OfferPhotoPath::getOfferId,
                        OfferPhotoPath::getImagePath));

        Map<Long, OfferProjection> offerById = offers.stream()
                .collect(toMap(OfferProjection::getId, identity()));

        return requests
                .map(req -> {
                    OfferProjection op = offerById.get(req.getOfferId());
                    String city = cityById.get(op.getCityId());
                    String path = photoByOffer.get(op.getId());
                    String url = fileStorageService.getPresignedUrl(path);

                    RentOfferDTO offerDto = new RentOfferDTO(
                            op.getId(), op.getTitle(), url,
                            op.getPrice(), op.getCurrency(), city
                    );

                    User u = userById.get(req.getUserId());
                    String lastName = u.getLastName() == null ? "" : u.getLastName();
                    RentUserDTO userDto = new RentUserDTO(
                            u.getId(),
                            (u.getFirstName() + " " + lastName).trim(),
                            u.getPhoneNumber()
                    );

                    Set<LocalDate> dates = req.getDates().stream()
                            .map(OfferCalendarProjection::getDate)
                            .collect(toSet());

                    return new RentRequestDTO(
                            req.getId(), req.getStatus(),
                            dates, req.getCreatedAt(),
                            req.getResolvedAt(),
                            offerDto, userDto
                    );
                });
    }
}
