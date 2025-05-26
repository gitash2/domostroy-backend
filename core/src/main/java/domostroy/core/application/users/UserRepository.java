package domostroy.core.application.users;

import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.LessorInfo;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Stream;

public interface UserRepository {
    User findById(Long id);
    User findByEmail(String email);
    void deleteById(Long id);
    void save(User user);
    List<User> findAllByIds(List<Long> ids);
    Page<User> searchUsers(String query, Pageable pageable);
    User findByOfferId(Long offerId);
}
