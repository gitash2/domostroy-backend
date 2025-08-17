package domostroy.core.adapters.adaptersOutput.feign.users;

import domostroy.core.adapters.adaptersOutput.users.projections.User;
import domostroy.core.application.users.UserRepository;
import domostroy.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFeignClient implements UserRepository {
    private final UserFeignDAO userFeignDAO;

    @Override
    public User findById(Long userId) {
        return userFeignDAO.findById(userId);
    }

    @Override
    public User findByEmail(String email) {
        return userFeignDAO.findByEmail(email);
    }

    @Override
    public void deleteById(Long id) {
        userFeignDAO.deleteById(id);
    }

    @Override
    public List<User> findAllByIds(List<Long> ids) {
        return userFeignDAO.findAllByIds(ids);
    }

    @Override
    public Page<User> searchUsers(String query, Pageable pageable) {
        return userFeignDAO.searchUsers(query, pageable);
    }
}
