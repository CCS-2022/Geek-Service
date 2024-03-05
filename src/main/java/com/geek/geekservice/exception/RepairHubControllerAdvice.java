package com.geek.geekservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class RepairHubControllerAdvice {
    @ExceptionHandler(RepairHubException.class)
    public ResponseEntity<ErrorResponse> handleGeekServerException(Exception e){
        // Casting generic exception to custom geek exception
        RepairHubException exception = (RepairHubException) e;

        HttpStatus status = exception.getStatus();

        // Creating StringWriter because it is needed to retrieve stack trace
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();

        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace), status);
    }
}
