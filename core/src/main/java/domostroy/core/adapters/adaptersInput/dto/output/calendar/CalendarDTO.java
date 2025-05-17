package domostroy.core.adapters.adaptersInput.dto.output.calendar;

import java.time.LocalDate;

public record CalendarDTO(
        LocalDate date,
        boolean isBooked
) {
}
