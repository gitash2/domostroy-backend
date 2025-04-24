package domostroy.core.adapters.adaptersInput.api.mobile;

import domostroy.core.adapters.adaptersInput.dto.input.users.ChangePasswordDTO;
import domostroy.core.adapters.adaptersInput.dto.input.users.UserDTO;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import domostroy.core.application.users.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "API мобильного приложения для работы с данными пользователей")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<UserDTO> getUserData(@AuthenticationPrincipal User user) {
        return ok(userService.getUserData(user.getId()));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @AuthenticationPrincipal User user,
            @RequestBody ChangePasswordDTO dto) {
        userService.changePassword(user.getId(), dto);
    }
}
