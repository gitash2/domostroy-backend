package domostroy.core.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ObjectNotFoundException extends RuntimeException {
    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
