package domostroy.auth.api;

import domostroy.users.domain.RoleModel;
import domostroy.auth.dto.users.input.ChangePasswordDTO;
import domostroy.auth.dto.users.input.ChangeUserInfoDTO;
import domostroy.auth.dto.users.output.UserNotificationFlag;
import domostroy.auth.users.model.User;
import domostroy.auth.users.service.UserService;
import domostroy.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserById(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserByEmail(@RequestParam String email) {
        return userService.findByEmail(email);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        // TODO send message into kafka topic remove all orders data
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.save(userDTO);
    }

    @GetMapping("/batch")
    @ResponseStatus(HttpStatus.OK)
    List<UserDTO> findAllByIds(@RequestBody List<Long> ids) {
        return userService.findAllByIds(ids);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    Page<UserDTO> searchUsers(@RequestParam String query, Pageable pageable) {
        return userService.searchUsers(query, pageable);
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

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void editNotifications(
            @RequestParam("notificationsEnabled") boolean notificationsEnabled,
            @AuthenticationPrincipal UserDetails user) {
        userService.editNotifications(user, notificationsEnabled);
    }

    @PatchMapping("/userInfo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Редактировать данные пользователя")
    public void changeUserInfo(
            @AuthenticationPrincipal User user,
            @RequestBody ChangeUserInfoDTO dto) {
        userService.changeUserInfo(user.getId(), dto);
    }

    @GetMapping("/notificationFlag")
    public ResponseEntity<UserNotificationFlag> getNotificationFlag(@AuthenticationPrincipal UserDetails user) {
        return ok(userService.getUserNotificationFlag(user));
    }

    @PostMapping("/users/{userId}/ban")
    @PreAuthorize(RoleModel.Allowed.ADMIN)
    @Operation(
            summary = "Забанить/разблокировать пользователя",
            description = "Блокирует/разблокирует пользователя по ID"
    )
    @ResponseStatus(HttpStatus.OK)
    public void banUser(@PathVariable Long userId, @RequestParam Boolean isBanned) {
        userService.banUser(userId, isBanned);
    }
}
