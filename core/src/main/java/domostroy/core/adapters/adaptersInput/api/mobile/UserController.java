package domostroy.core.adapters.adaptersInput.api.mobile;

import domostroy.core.adapters.adaptersInput.dto.output.users.AnotherUserDTO;
import domostroy.core.adapters.adaptersInput.dto.output.users.UserDTO;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import domostroy.core.application.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "API мобильного приложения для работы с данными пользователей")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDTO> getUserData(@AuthenticationPrincipal User user) {
        return ok(userService.getCurrentUserData(user.getId()));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Получить данные другого пользователя")
    public ResponseEntity<AnotherUserDTO> getUser(@PathVariable Long userId, @Nullable @AuthenticationPrincipal UserDetails user) {
        return ok(userService.getUserData(userId, user));
    }
}
