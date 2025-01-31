package com.taxah.hspd.controller.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.taxah.hspd.entity.log.ErrorEntity;
import com.taxah.hspd.exception.AlreadyExistsException;
import com.taxah.hspd.exception.JwtRefreshExpiredException;
import com.taxah.hspd.exception.NotFoundException;
import com.taxah.hspd.exception.UnsupportedException;
import com.taxah.hspd.exception.dto.StringErrorDTO;
import com.taxah.hspd.exception.dto.ValidationErrorDTO;
import com.taxah.hspd.repository.log.ErrorEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

import static com.taxah.hspd.util.constant.Exceptions.*;


@Order(10)
@ControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler implements UserAccessHandler {
    public static final String EXTRA_PREFIX = "extra: ";
    public static final String REFRESH_PREFIX = "Will be deleted Refresh";

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

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<StringErrorDTO> handleInvalidFormatException(InvalidFormatException e) {
        if (e.getTargetType().equals(java.time.LocalDate.class)) {
            return stringResponseEntity(e, HttpStatus.BAD_REQUEST, INVALID_DATE_FORMAT, false);
        }
        return stringResponseEntity(e, HttpStatus.BAD_REQUEST, INVALID_INPUT_FORMAT + e.getValue());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<StringErrorDTO> handleAccessDenied(AuthorizationDeniedException e) {
        return stringResponseEntity(e, HttpStatus.FORBIDDEN, null);
    }

    @ExceptionHandler(JwtRefreshExpiredException.class)
    public ResponseEntity<StringErrorDTO> handleJwtRefreshExpiredException(JwtRefreshExpiredException e) {
        return stringResponseEntity(e, HttpStatus.FORBIDDEN, REFRESH_PREFIX, false);
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
                .errorMessage(extra != null ? extra + " " + e.getMessage() : e.getMessage())
                .build());
    }
}
