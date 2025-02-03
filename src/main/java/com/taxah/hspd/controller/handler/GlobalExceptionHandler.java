package com.taxah.hspd.controller.handler;

import com.taxah.hspd.entity.log.ErrorEntity;
import com.taxah.hspd.exception.dto.ExceptionErrorDTO;
import com.taxah.hspd.repository.log.ErrorEntityRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.taxah.hspd.util.constant.Exceptions.MESSAGE_PREFIX;

@Order(20)
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler implements UserAccessHandler {
    public static final String TRACE_PREFIX = "\n\ttrace: ";

    private final ErrorEntityRepository errorRepository;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionErrorDTO> handleGlobalException(Exception e) {
        return exceptionResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
    }

    private ResponseEntity<ExceptionErrorDTO> exceptionResponseEntity(Exception e, HttpStatus status, String trace) {
        String username = getAuthenticationUsername();
        ErrorEntity error = errorRepository.save(ErrorEntity.builder()
                .username(username)
                .message(MESSAGE_PREFIX + e.getMessage() + TRACE_PREFIX + trace)
                .build());
        return ResponseEntity.status(status).body(ExceptionErrorDTO.builder()
                .errorUUID(error.getId())
                .errorMessage(e.getMessage())
                .trace(trace)
                .build());
    }
}
