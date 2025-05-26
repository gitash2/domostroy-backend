package domostroy.core.exceptions;

import domostroy.core.exceptions.errors.DBError;
import domostroy.core.exceptions.errors.GeneralError;
import domostroy.core.exceptions.errors.LogicalError;
import domostroy.core.exceptions.errors.ValidationError;
import domostroy.core.exceptions.rentRequest.InvalidRentRequestException;
import domostroy.core.exceptions.rentRequest.InvalidStatusChangeException;
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

    @ExceptionHandler(InvalidRentRequestException.class)
    public ResponseEntity<GeneralError> handleInvalidRentRequestException(InvalidRentRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ValidationError(Set.of(ex.getLocalizedMessage())));
    }

    @ExceptionHandler(InvalidStatusChangeException.class)
    public ResponseEntity<GeneralError> handleInvalidStatusChangeException(InvalidStatusChangeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new LogicalError(Set.of(ex.getLocalizedMessage())));
    }

    @ExceptionHandler(ModerationException.class)
    public ResponseEntity<GeneralError> handleModerationException(ModerationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ValidationError(Set.of(ex.getLocalizedMessage())));
    }


}
