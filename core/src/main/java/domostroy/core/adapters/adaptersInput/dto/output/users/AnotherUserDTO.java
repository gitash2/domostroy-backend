package domostroy.core.adapters.adaptersInput.dto.output.users;

import java.time.LocalDate;

public record AnotherUserDTO(
        Long userId,
        String firstName,
        String lastName,
        Integer numOfOffers,
        LocalDate createdAt
) {
}
