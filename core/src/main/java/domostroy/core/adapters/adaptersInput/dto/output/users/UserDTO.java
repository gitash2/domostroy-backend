package domostroy.core.adapters.adaptersInput.dto.output.users;

public record UserDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String role,
        Boolean isBanned
) {
}
