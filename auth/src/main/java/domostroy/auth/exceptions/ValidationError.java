package domostroy.auth.exceptions;

import java.util.Set;

public final class ValidationError extends GeneralError {
    public ValidationError(Set<String> subErrors) {
        super("Date validation error", subErrors);
    }
}
