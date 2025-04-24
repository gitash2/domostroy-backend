package domostroy.core.exceptions.errors;

import java.util.Set;

public final class IntegrationError extends GeneralError {
    public IntegrationError(Set<String> subErrors) {
        super("Ошибка взаимодействия сервисов", subErrors);
    }
}
