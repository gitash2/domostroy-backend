package domostroy.auth.exceptions;

import domostroy.auth.constants.ExMessages;
import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException() {
        super(ExMessages.NOT_VALID_TOKEN_EXCEPTION_MESSAGE);
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}

