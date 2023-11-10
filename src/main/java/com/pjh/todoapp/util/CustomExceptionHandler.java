package util;

import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<?> handleApiRequestException(ApiRequestException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        String errorMessage = ex.getMessage();
        return new ResponseEntity<>(errorMessage, httpStatus);
    }
}
