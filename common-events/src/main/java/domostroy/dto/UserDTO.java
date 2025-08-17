package domostroy.dto;

import domostroy.users.domain.Role;

import java.time.LocalDateTime;

public record UserDTO(
        Long id,
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber,
        Role role,
        LocalDateTime createdAt,
        Boolean notificationsEnabled,
        Boolean isBanned
) {
}
