package domostroy.core.application.deepseek;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domostroy.core.adapters.adaptersInput.dto.output.offers.ChatRequest;
import domostroy.core.adapters.adaptersInput.dto.output.offers.ModerationResponse;
import domostroy.core.adapters.adaptersInput.dto.output.offers.OpenRouterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
@Log4j2
public class LLMService {
    private final WebClient webClient;

    public Mono<ModerationResponse> checkText(String title, String description) {
        String prompt = """
                Проверь следующий заголовок и описание объявления на наличие нецензурной лексики, оскорблений или неприемлемого содержания.
                
                
                            Ответь **строго в формате JSON**, **без пояснений, комментариев или форматирования**. Не используй markdown (например, ```json), не добавляй вводных фраз.
                            Формат ответа:
                            {"valid": true|false, "reason": "строка или null"}
                
                            Заголовок: %s \s
                            Описание: %s
                
                """.formatted(title, description);

        ChatRequest request = new ChatRequest(
                "qwen/qwen3-30b-a3b:free",
                prompt
        );

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    if (response.statusCode().value() == 429) {
                        log.warn("429 Too Many Requests — пропускаем модерацию");
                        return Mono.empty();
                    }
                    return response.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("API Error: {}", errorBody);
                                return Mono.error(new RuntimeException("OpenRouter Error: " + errorBody));
                            });
                })
                .bodyToMono(OpenRouterResponse.class)
                .map(response -> {
                    if (response.getChoices() == null || response.getChoices().isEmpty()) {
                        log.warn("Ответ OpenRouter пустой или choices отсутствуют, пропускаем модерацию");
                        return new ModerationResponse(true, "Модерация пропущена: нет результата от нейросети");
                    }

                    String content = response.getChoices().get(0).getText();
                    log.debug("Raw content from OpenRouter: {}", content);

                    String cleaned = content
                            .replaceAll("^```json\\s*", "")
                            .replaceAll("```\\s*$", "")
                            .trim();

                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        return mapper.readValue(cleaned, ModerationResponse.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Failed to parse moderation result: " + cleaned, e);
                    }
                })
                .switchIfEmpty(Mono.just(new ModerationResponse(true, "Модерация пропущена: лимит токенов исчерпан")));


    }
}
