package com.taxah.hspd.controller.handler;

import com.taxah.hspd.exception.AlreadyExistsException;
import com.taxah.hspd.exception.NotFoundException;
import com.taxah.hspd.exception.dto.StringErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.UUID;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<StringErrorDTO> handleUsernameAlreadyExistException(Exception e) {
        return stringResponseEntity(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(exception = {
            NotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<StringErrorDTO> handleNotFoundException(Exception e) {
        return stringResponseEntity(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StringErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> response = ex.getBindingResult().getFieldErrors().stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage()
                )
                .toList();

        return stringResponseEntity(HttpStatus.BAD_REQUEST, response.toString());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<StringErrorDTO> handleAccessDenied(Exception e) {
        return stringResponseEntity(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StringErrorDTO> handleGlobalException(Exception e) {
        return stringResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private ResponseEntity<StringErrorDTO> stringResponseEntity(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(StringErrorDTO.builder()
                .errorUUID(UUID.randomUUID())
                .errorMessage(message)
                .build());
    }
}
