package domostroy.core.adapters.adaptersInput.dto.output.offers;

import lombok.Data;

import java.util.List;

@Data
public class OpenRouterResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private String text;
    }
}
