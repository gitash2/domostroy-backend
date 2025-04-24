package domostroy.core.adapters.adaptersOutput.users.dao;

import domostroy.core.adapters.adaptersOutput.users.projections.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
