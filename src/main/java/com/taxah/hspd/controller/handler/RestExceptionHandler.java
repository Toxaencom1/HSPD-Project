package com.taxah.hspd.controller.handler;

import com.taxah.hspd.exception.AlreadyExistsException;
import com.taxah.hspd.exception.UserNotFoundException;
import com.taxah.hspd.exception.dto.StringErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
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

    @ExceptionHandler(exception = {UserNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<StringErrorDTO> handleUserNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StringErrorDTO.builder()
                .errorUUID(UUID.randomUUID())
                .errorMessage(e.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StringErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> response = ex.getBindingResult().getFieldErrors().stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage()
                )
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringErrorDTO.builder()
                .errorUUID(UUID.randomUUID())
                .errorMessage(response.toString())
                .build());
    }
}
