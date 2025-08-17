package domostroy.core.application.users;

import domostroy.core.adapters.adaptersOutput.users.projections.User;
import domostroy.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepository {

    User findById(Long userId);
    User findByEmail(String email);
    void deleteById(Long id);
    List<User> findAllByIds(List<Long> ids);
    Page<User> searchUsers(String query, Pageable pageable);
}
