package com.geek.geekservice.config;

import com.geek.geekservice.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GRHControllerAdvice {

    // Handler for Invalid JSON or NonNull SO fields check
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception e){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        log.error(status + " " + e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), "N/A"), status);
    }
}
