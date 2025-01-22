package com.taxah.hspd.controller.handler;

import com.taxah.hspd.entity.log.ErrorEntity;
import com.taxah.hspd.exception.AlreadyExistsException;
import com.taxah.hspd.exception.NotFoundException;
import com.taxah.hspd.exception.UnsupportedException;
import com.taxah.hspd.exception.dto.StringErrorDTO;
import com.taxah.hspd.exception.dto.ValidationErrorDTO;
import com.taxah.hspd.repository.log.ErrorEntityRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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
@RequiredArgsConstructor
public class RestExceptionHandler implements UserAccessHandler {

    public static final String EXTRA_PREFIX = "extra: ";
    public static final String MESSAGE_PREFIX = "message: ";
    public static final String TRACE_PREFIX = "\n\ttrace: ";
    private final ErrorEntityRepository errorRepository;

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<StringErrorDTO> handleUsernameAlreadyExistException(AlreadyExistsException e) {
        return stringResponseEntity(e, HttpStatus.CONFLICT, null);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StringErrorDTO> handleNotFoundException(Exception e) {
        return stringResponseEntity(e, HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StringErrorDTO> handleBadCredentialsException(BadCredentialsException e) {
        return stringResponseEntity(e, HttpStatus.NOT_FOUND, null, false);
    }

    @ExceptionHandler(UnsupportedException.class)
    public ResponseEntity<StringErrorDTO> handleUnsupportedException(UnsupportedException e) {
        return stringResponseEntity(e, HttpStatus.UNPROCESSABLE_ENTITY, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDTO> handleValidationExceptions(MethodArgumentNotValidException e) {
        ValidationErrorDTO errorDTO = ValidationErrorDTO.builder()
                .errorUUID(UUID.randomUUID())
                .errors(e.getBindingResult().getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList()
                )
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<StringErrorDTO> handleAccessDenied(AuthorizationDeniedException e) {
        return stringResponseEntity(e, HttpStatus.FORBIDDEN, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StringErrorDTO> handleGlobalException(Exception e) {
        String extra = TRACE_PREFIX + ExceptionUtils.getStackTrace(e);
        return stringResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR, extra, true);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<StringErrorDTO> handleNullPointerException(NullPointerException e) {
        return stringResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    private ResponseEntity<StringErrorDTO> stringResponseEntity(Exception e, HttpStatus status, String extra) {
        String username = getAuthenticationUsername();
        String extraString = extra != null ? EXTRA_PREFIX + extra + "\n" : "";
        ErrorEntity error = errorRepository.save(ErrorEntity.builder()
                .username(username)
                .message(
                        extraString + MESSAGE_PREFIX + e.getMessage()
                )
                .build());
        return ResponseEntity.status(status).body(StringErrorDTO.builder()
                .errorUUID(error.getId())
                .errorMessage(extra != null ? extra : e.getMessage())
                .build());
    }

    private ResponseEntity<StringErrorDTO> stringResponseEntity(Exception e, HttpStatus status, String extra, boolean toDatabaseLog) {
        return toDatabaseLog
                ? stringResponseEntity(e, status, extra)
                : ResponseEntity.status(status).body(StringErrorDTO.builder()
                    .errorUUID(UUID.randomUUID())
                    .errorMessage(extra != null ? extra : e.getMessage())
                    .build());
    }
}
