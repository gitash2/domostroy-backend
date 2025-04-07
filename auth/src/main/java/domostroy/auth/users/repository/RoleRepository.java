package domostroy.auth.users.repository;

import domostroy.auth.users.model.Role;
import domostroy.auth.users.model.RoleValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRole(RoleValue role);
}
