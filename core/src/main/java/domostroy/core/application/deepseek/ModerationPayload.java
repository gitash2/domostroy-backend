package domostroy.core.application.deepseek;

public record ModerationPayload(
        Long offerId,
        String title,
        String description
) {
}
