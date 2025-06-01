package domostroy.core.adapters.adaptersInput.dto.output.offers;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModerationResponse {
    private Boolean valid;
    private String reason;
}

