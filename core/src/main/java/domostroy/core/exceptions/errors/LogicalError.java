package domostroy.core.exceptions.errors;

import java.util.Set;

public final class LogicalError extends GeneralError {
    public LogicalError(Set<String> subErrors) {
        super("Logical error", subErrors);
    }
}