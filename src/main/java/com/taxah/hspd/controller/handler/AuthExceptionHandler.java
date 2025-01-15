package com.taxah.hspd.controller.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthExceptionHandler implements AuthenticationEntryPoint {

    public static final String TIMESTAMP = "timestamp";
    public static final String STATUS = "status";
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";
    public static final String PATH = "path";
    public static final String FORBIDDEN = "Forbidden";
    public static final String USER_IS_NOT_AUTHENTICATED = "User is not authenticated. Please log in.";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now().toString());
        body.put(STATUS, HttpStatus.FORBIDDEN.value());
        body.put(ERROR, FORBIDDEN);
        body.put(MESSAGE, USER_IS_NOT_AUTHENTICATED);
        body.put(PATH, request.getRequestURI());

        response.getOutputStream().println(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(body));
    }
}
