package domostroy.core.adapters.adaptersOutput.users.dao;

import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.LessorInfo;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDAO extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("""
                  SELECT new domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.LessorInfo(
                  u.id, u.firstName)
                  FROM User u
                  WHERE u.id IN :ids
            """)
    List<LessorInfo> findAllByIds(List<Long> ids);
}
