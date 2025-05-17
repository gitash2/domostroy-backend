package domostroy.auth.exceptions;


import java.util.Set;

public final class LogicalError extends GeneralError {
    public LogicalError(Set<String> subErrors) {
        super("Logical error", subErrors);
    }
}