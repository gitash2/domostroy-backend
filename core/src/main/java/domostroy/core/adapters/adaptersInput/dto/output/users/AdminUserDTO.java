package domostroy.core.adapters.adaptersInput.dto.output.users;

import java.time.LocalDate;

public record AdminUserDTO(
        Long id,
        String name,
        String email,
        String phoneNumber,
        Integer numOfOffers,
        boolean isBanned,
        String role,
        LocalDate createdAt
) {
}
