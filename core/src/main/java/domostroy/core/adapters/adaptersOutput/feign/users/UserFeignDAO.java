package domostroy.core.adapters.adaptersOutput.feign.users;

import domostroy.core.adapters.adaptersOutput.users.projections.User;
import domostroy.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "auth-service", url = "${services.auth-service.url}")
public interface UserFeignDAO {

    @GetMapping("/users/{userId}")
    User findById(@PathVariable Long userId);

    @GetMapping("/users")
    User findByEmail(@RequestParam String email);

    @DeleteMapping("/users/{id}")
    void deleteById(@PathVariable Long id);

    @PostMapping("/users/batch")
    List<User> findAllByIds(@RequestBody List<Long> ids);

    @GetMapping("/users/search")
    Page<User> searchUsers(@RequestParam String query, Pageable pageable);


}
