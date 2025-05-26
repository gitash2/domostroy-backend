package domostroy.core.adapters.adaptersInput.dto.output.offers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModerationResponse {
    private boolean valid;
    private String reason;
}

