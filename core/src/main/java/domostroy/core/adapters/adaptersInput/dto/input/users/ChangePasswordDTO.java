package domostroy.core.adapters.adaptersInput.dto.input.users;

public record ChangePasswordDTO(
        String previousPassword,
        String newPassword
) {
}
