package com.taxah.hspd.controller.handler;

import com.taxah.hspd.exception.AlreadyExistsException;
import com.taxah.hspd.exception.UserNotFoundException;
import com.taxah.hspd.exception.dto.StringErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<StringErrorDTO> handleUsernameAlreadyExistException(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(StringErrorDTO.builder()
                .errorUUID(UUID.randomUUID())
                .errorMessage(e.getMessage())
                .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<StringErrorDTO> handleUserNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StringErrorDTO.builder()
                .errorUUID(UUID.randomUUID())
                .errorMessage(e.getMessage())
                .build());
    }
}
