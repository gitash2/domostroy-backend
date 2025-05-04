package domostroy.core.adapters.adaptersInput.dto.input.mobile.users;

public record ChangePasswordDTO(
        String previousPassword,
        String newPassword
) {
}
