package domostroy.auth.dto.users.input;

public record ChangeUserInfoDTO(
        String firstName,
        String lastName,
        String phoneNumber
) {
}
