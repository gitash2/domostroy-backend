package domostroy.auth.exceptions;


import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public abstract sealed class GeneralError permits ValidationError, LogicalError {
    private static final Logger logger = LogManager.getLogger(GeneralError.class);
    private final String cause;
    private final Set<String> subErrors;
    private final LocalDateTime time;

    protected GeneralError(String cause, Set<String> subErrors, LocalDateTime time) {
        this.cause = cause;
        this.subErrors = Set.copyOf(subErrors);
        this.time = time;
        logger.error("Error at {}: {}, Details: {}", time, cause, subErrors);
    }

    protected GeneralError(String cause, Set<String> subErrors) {
        this(cause, subErrors, LocalDateTime.now());
    }

    protected GeneralError() {
        this("Server error", Set.of("Unknown"));
    }

}
