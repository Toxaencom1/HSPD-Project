package com.taxah.hspd.controller.handler;

import com.taxah.hspd.entity.log.ErrorEntity;
import com.taxah.hspd.exception.AlreadyExistsException;
import com.taxah.hspd.exception.NotFoundException;
import com.taxah.hspd.exception.dto.StringErrorDTO;
import com.taxah.hspd.repository.log.ErrorEntityRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler implements UserAccessHandler {

    private final ErrorEntityRepository errorRepository;

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<StringErrorDTO> handleUsernameAlreadyExistException(Exception e) {
        return stringResponseEntity(e, HttpStatus.CONFLICT, null);
    }

    @ExceptionHandler(exception = {
            NotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<StringErrorDTO> handleNotFoundException(Exception e) {
        return stringResponseEntity(e, HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StringErrorDTO> handleValidationExceptions(MethodArgumentNotValidException e) {
        List<String> response = e.getBindingResult().getFieldErrors().stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage()
                )
                .toList();
        return stringResponseEntity(e, HttpStatus.BAD_REQUEST, response.toString());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<StringErrorDTO> handleAccessDenied(Exception e) {
        return stringResponseEntity(e, HttpStatus.FORBIDDEN, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StringErrorDTO> handleGlobalException(Exception e) {
        String extra = "\n\ttrace: " + ExceptionUtils.getStackTrace(e);
        return stringResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR, extra);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<StringErrorDTO> handleNullPointerException(Exception e) {
        return stringResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    private ResponseEntity<StringErrorDTO> stringResponseEntity(Exception e, HttpStatus status, String extra) {
        String username = getAuthenticationUsername();
        ErrorEntity error = errorRepository.save(ErrorEntity.builder()
                .username(username)
                .message(
                        (extra != null ? "extra: " + extra + "\n" : "") + "message: " + e.getMessage()
                )
                .build());
        return ResponseEntity.status(status).body(StringErrorDTO.builder()
                .errorUUID(error.getId())
                .errorMessage(extra != null ? extra : e.getMessage())
                .build());
    }
}
