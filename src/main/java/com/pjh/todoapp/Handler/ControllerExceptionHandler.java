package com.pjh.todoapp.Handler;

import com.pjh.todoapp.Entity.CMRespDto;
import com.pjh.todoapp.Handler.ex.CustomValidationApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
// @ControllerAdvice 모든 Exception(예외처리)를 다 처리함
public class ControllerExceptionHandler {

    @ExceptionHandler(CustomValidationApiException.class)
    public ResponseEntity<?> validationException(CustomValidationApiException e) {
        return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }
}
