package domostroy.auth.dto.users.input;

public record ChangePasswordDTO(
        String previousPassword,
        String newPassword
) {
}
