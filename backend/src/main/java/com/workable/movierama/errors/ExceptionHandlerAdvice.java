package com.workable.movierama.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(ValidationError.class)
    public ResponseEntity<ApiError> handleEntityNotFound(ValidationError ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST, ex.getErrorCode()), HttpStatus.BAD_REQUEST);
    }
}
