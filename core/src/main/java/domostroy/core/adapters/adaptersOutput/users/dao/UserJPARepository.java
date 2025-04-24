package domostroy.core.adapters.adaptersOutput.users.dao;

import domostroy.aggregates.users.domain.UserAggregate;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import domostroy.core.application.users.UserRepository;
import domostroy.core.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserJPARepository implements UserRepository {
    private final UserDAO userDAO;

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

    }

    @Override
    public void save(User user) {

    }
}
