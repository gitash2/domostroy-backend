package domostroy.core.adapters.adaptersOutput.users.dao;

import domostroy.core.adapters.adaptersOutput.offerPhotos.dao.OfferPhotoDAO;
import domostroy.core.adapters.adaptersOutput.offers.dao.OfferDAO;
import domostroy.core.adapters.adaptersOutput.rentRequest.dao.RentRequestDAO;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import domostroy.core.application.cloudStorage.FileStorageService;
import domostroy.core.application.users.UserRepository;
import domostroy.core.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserJPARepository implements UserRepository {
    private final UserDAO userDAO;
    private final OfferDAO offerDAO;
    private final RentRequestDAO rentRequestDAO;
    private final OfferPhotoDAO offerPhotoDAO;
    private final FileStorageService fileStorageService;


    @Override
    public User findById(Long userId) {
        return userDAO.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("User with id " + userId + " not found")
        );
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    public void deleteById(Long id) {
        List<Long> rentRequestIds = rentRequestDAO.findAllRentRequestIdsByUserId(id);
        List<Long> offerIds = offerDAO.findAllOfferIdsByUserId(id);
        List<String> paths = offerPhotoDAO.findAllPhotosPathsByOfferIds(offerIds);
        userDAO.deleteAllFavouritesByUserId(id);
        userDAO.deleteRentRequestDatesByRequestIds(rentRequestIds);
        userDAO.deleteOfferCalendarsByOfferIds(offerIds);
        userDAO.deleteRentRequestByUserId(id);
        userDAO.deleteOffersPhotosByOfferIds(offerIds);
        userDAO.deleteOffersByUserId(id);
        userDAO.deleteUserByUserId(id);
        fileStorageService.deleteFiles(paths);
    }

    @Override
    public void save(User user) {

    }

    @Override
    public List<User> findAllByIds(List<Long> ids) {
        return userDAO.findAllByIds(ids);
    }

    @Override
    public Page<User> searchUsers(String query, Pageable pageable) {
        return userDAO.searchUsers(query, pageable);
    }

    @Override
    public User findByOfferId(Long offerId) {
        return userDAO.findUserByOfferId(offerId);
    }
}
