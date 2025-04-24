package domostroy.core.exceptions.errors;

import java.util.Set;

public final class DBError extends GeneralError {
    public DBError(Set<String> subErrors) {
        super("Ошибка взаимодействия с БД", subErrors);
    }
}
