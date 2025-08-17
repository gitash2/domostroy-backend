package domostroy.auth.users.repository.dao;

import domostroy.auth.users.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserJPARepository implements UserRepository {
    private final UserDAO userDAO;

    @Override
    public boolean existsByEmail(String email) {
        return userDAO.existsByEmail(email);
    }

    @Override
    public User findByEmail(String username) {
        return userDAO.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public User save(User user) {
        return userDAO.save(user);
    }

    @Override
    public User findById(Long id) {
        return userDAO.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
    }

    @Override
    public void deleteById(Long id) {
        userDAO.deleteById(id);
    }

    @Override
    public List<User> findAllByIds(List<Long> ids) {
        return userDAO.findAllById(ids);
    }

    @Override
    public Page<User> searchUsers(String query, Pageable pageable) {
        return userDAO.searchUsers(query, pageable);
    }


}
