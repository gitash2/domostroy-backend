package domostroy.core.exceptions;

import domostroy.core.exceptions.errors.DBError;
import domostroy.core.exceptions.errors.GeneralError;
import domostroy.core.exceptions.errors.LogicalError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<GeneralError> handleObjectNotFoundError(ObjectNotFoundException ex) {
        return ResponseEntity.status(ex.getHttpStatus())
                .body(new DBError(Set.of(ex.getLocalizedMessage())));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<GeneralError> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new LogicalError(Set.of(ex.getLocalizedMessage())));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GeneralError> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new LogicalError(Set.of(ex.getLocalizedMessage())));
    }


}
