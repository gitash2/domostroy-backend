package domostroy.core.application.users;

import domostroy.aggregates.users.domain.UserAggregate;
import domostroy.core.adapters.adaptersOutput.users.projections.User;

public interface UserRepository {
    User findById(Long id);
    User findByEmail(String email);
    void deleteById(Long id);
    void save(User user);
}
