package domostroy.core.exceptions.errors;


import java.util.Set;

public final class ValidationError extends GeneralError {
    public ValidationError(Set<String> subErrors) {
        super("Ошибка валидации данных", subErrors);
    }
}
