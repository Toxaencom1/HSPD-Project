package com.taxah.hspd.controller;

import com.taxah.hspd.exception.AlreadyExistsException;
import com.taxah.hspd.exception.error.StringError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<StringError> handleUsernameAlreadyExistException(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(StringError.builder()
                .errorUUID(UUID.randomUUID())
                .errorMessage(e.getMessage())
                .build());
    }
}
