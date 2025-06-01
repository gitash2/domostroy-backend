package domostroy.core.application.deepseek;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domostroy.core.adapters.adaptersInput.dto.output.offers.ChatRequest;
import domostroy.core.adapters.adaptersInput.dto.output.offers.ModerationResponse;
import domostroy.core.adapters.adaptersInput.dto.output.offers.OpenRouterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Log4j2
public class LLMService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper(); // thread-safe

    public Mono<ModerationResponse> checkText(String title, String description) {
        String prompt = """
                Проверь следующий заголовок и описание объявления на наличие нецензурной лексики, оскорблений или неприемлемого содержания.
                Формат ответа:
                {"valid": true|false, "reason": "строка|null"}
                Заголовок: %s \s
                Описание: %s
                """.formatted(title, description);

        ChatRequest request = new ChatRequest(
                "mistralai/devstral-small:free",
                prompt
        );

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    HttpStatus status = (HttpStatus) response.statusCode();
                    if (status == HttpStatus.TOO_MANY_REQUESTS || status == HttpStatus.UNAUTHORIZED || status == HttpStatus.FORBIDDEN) {
                        log.warn("{} {} — пропускаем модерацию", status.value(), status.getReasonPhrase());
                        return Mono.empty(); // switchIfEmpty обработает
                    }

                    return response.bodyToMono(String.class)
                            .defaultIfEmpty("Empty error body")
                            .flatMap(errorBody -> {
                                log.error("Ошибка ответа от OpenRouter ({}): {}", status.value(), errorBody);
                                return Mono.error(new RuntimeException("Ошибка OpenRouter: " + errorBody));
                            });
                })
                .bodyToMono(OpenRouterResponse.class)
                .flatMap(response -> {
                    if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
                        log.warn("Пустой ответ от OpenRouter или отсутствуют choices — пропускаем модерацию");
                        return Mono.just(new ModerationResponse(true, "Модерация пропущена: пустой ответ от модели"));
                    }

                    String rawContent = response.getChoices().get(0).getText();
                    log.debug("Сырой ответ модели: {}", rawContent);

                    String cleaned = rawContent
                            .replaceAll("^```json\\s*", "")
                            .replaceAll("^```\\s*", "")
                            .replaceAll("```\\s*$", "")
                            .trim();

                    try {
                        ModerationResponse parsed = objectMapper.readValue(cleaned, ModerationResponse.class);
                        if (parsed.getValid() == null) {
                            log.warn("Поля 'valid' нет или null в JSON от модели: {}", cleaned);
                            return Mono.just(new ModerationResponse(true, "Модерация пропущена: invalid формат ответа"));
                        }
                        return Mono.just(parsed);
                    } catch (JsonProcessingException e) {
                        log.error("Ошибка парсинга JSON от модели: {}\nИсключение: {}", cleaned, e.getMessage());
                        return Mono.just(new ModerationResponse(true, "Модерация пропущена: ошибка разбора JSON"));
                    }
                })
                .onErrorResume(throwable -> {
                    log.error("Исключение при выполнении модерации: {}", throwable.getMessage(), throwable);
                    return Mono.just(new ModerationResponse(true, "Модерация пропущена: внутренняя ошибка"));
                })
                .switchIfEmpty(Mono.just(new ModerationResponse(true, "Модерация пропущена: лимит или отказ доступа")));
    }
}
