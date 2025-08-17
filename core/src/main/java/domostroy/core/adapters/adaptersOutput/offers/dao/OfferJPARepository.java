package domostroy.core.adapters.adaptersOutput.offers.dao;

import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import domostroy.core.application.offers.OfferRepository;
import domostroy.core.application.users.UserRepository;
import domostroy.core.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OfferJPARepository implements OfferRepository {
    private final OfferDAO offerDAO;
    private final UserRepository userRepository;

    @Override
    public OfferProjection getOfferById(Long offerId) {
        return offerDAO.findById(offerId).orElseThrow(
                () -> new ObjectNotFoundException("Offer with id" + offerId + "is not found"));
    }

    @Override
    public void deleteOffer(Long offerId) {
        offerDAO.deleteById(offerId);

    }

    public Page<OfferProjection> getMyOffers(Long userId, Pageable pageable) {
        return offerDAO.getOffersByUserId(userId, pageable);
    }

    @Override
    public Page<OfferProjection> findAll(Specification<OfferProjection> spec, Pageable pageable) {
        return offerDAO.findAll(spec, pageable);
    }

    @Override
    public Page<OfferProjection> findFavouriteOffersByUserId(Long userId, Pageable pageable) {
        return offerDAO.findFavouriteOffersByUserId(userId, pageable);
    }

    @Override
    public boolean isMyOffer(Long offerId, String email) {
        return Objects.equals(offerDAO.findById(offerId).orElseThrow(
                () -> new ObjectNotFoundException("Offer with id: " + offerId + "is not found"))
                        .getUserId(),
                userRepository.findByEmail(email).getId());
    }

    @Override
    public boolean isFavourite(Long offerId, String email) {
        return offerDAO.isFavourite(offerId, email);
    }

    @Override
    public int getMyOffersCount(Long userId) {
        return offerDAO.getMyOffersCount(userId);
    }

    @Override
    public List<OfferProjection> getMyOffersIds(Long userId) {
        return offerDAO.getMyOffersIds(userId);
    }

    @Override
    public List<OfferProjection> findAllByIds(List<Long> offerIds) {
        return offerDAO.findAllById(offerIds);
    }

    @Override
    public OfferProjection findById(Long offerId) {
        return offerDAO.findOfferById(offerId)
                .orElseThrow(() -> new ObjectNotFoundException("Object with id " + offerId + " is not found"));
    }

    @Override
    public OfferProjection save(OfferProjection projection) {
        return offerDAO.save(projection);
    }
}
