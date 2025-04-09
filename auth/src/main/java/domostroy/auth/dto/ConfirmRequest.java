package domostroy.auth.dto;

public record ConfirmRequest(
        String email,
        String password,
        String confirmationCode
) {
}
