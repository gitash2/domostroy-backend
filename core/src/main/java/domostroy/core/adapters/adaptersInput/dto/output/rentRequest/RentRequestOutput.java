package domostroy.core.adapters.adaptersInput.dto.output.rentRequest;

import java.util.List;

public record RentRequestOutput(
        List<RentRequestDTO> requests
) {
}
