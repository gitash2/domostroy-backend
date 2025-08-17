package domostroy.auth.users.repository.dao;

import domostroy.auth.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserRepository {
    boolean existsByEmail(String email);

    User findByEmail(String username);

    User save(User user);

    User findById(Long id);

    void deleteById(Long id);

    List<User> findAllByIds(List<Long> ids);

    Page<User> searchUsers(String query, Pageable pageable);

}
