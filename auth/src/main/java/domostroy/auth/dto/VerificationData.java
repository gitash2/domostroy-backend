package domostroy.auth.dto;

public record VerificationData(
        String password,
        String verificationCode,
        String firstName,
        String lastName,
        String phoneNumber
) {
}
