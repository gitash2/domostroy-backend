package domostroy.core.adapters.adaptersInput.api.mobile;

import domostroy.aggregates.users.domain.RoleModel;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.users.ChangePasswordDTO;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.users.ChangeUserInfoDTO;
import domostroy.core.adapters.adaptersInput.dto.output.users.AnotherUserDTO;
import domostroy.core.adapters.adaptersInput.dto.output.users.UserDTO;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import domostroy.core.application.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Сменить пароль",
               description = "Смена пароля с проверкой на корректность предыдущего")
    public void changePassword(
            @AuthenticationPrincipal User user,
            @RequestBody ChangePasswordDTO dto) {
        userService.changePassword(user.getId(), dto);
    }

    @PatchMapping("/userInfo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Редактировать данные пользователя")
    public void changeUserInfo(
            @AuthenticationPrincipal User user,
            @RequestBody ChangeUserInfoDTO dto) {
        userService.changeUserInfo(user.getId(), dto);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Получить данные другого пользователя")
    public ResponseEntity<AnotherUserDTO> getUser(@PathVariable Long userId, @Nullable @AuthenticationPrincipal UserDetails user) {
        return ok(userService.getUserData(userId, user));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void editNotifications(
            @RequestParam("notificationsEnabled") boolean notificationsEnabled,
            @AuthenticationPrincipal UserDetails user) {
        userService.editNotifications(user, notificationsEnabled);
    }
}
