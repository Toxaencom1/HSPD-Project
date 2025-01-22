package com.taxah.hspd.controller.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxah.hspd.entity.log.ErrorEntity;
import com.taxah.hspd.exception.dto.StringErrorDTO;
import com.taxah.hspd.repository.log.ErrorEntityRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthExceptionHandler implements AuthenticationEntryPoint {
    public static final String ANONYMOUS_USERNAME = "anonymousUser";
    public static final String USER_IS_NOT_AUTHENTICATED = "User is not authenticated. Please log in.";
    private final ErrorEntityRepository errorEntityRepository;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ErrorEntity error = errorEntityRepository.save(ErrorEntity.builder()
                .username(ANONYMOUS_USERNAME)
                .message(USER_IS_NOT_AUTHENTICATED)
                .build());
        StringErrorDTO stringErrorDTO = StringErrorDTO.builder()
                .errorUUID(error.getId())
                .errorMessage(USER_IS_NOT_AUTHENTICATED).build();
        response.getOutputStream().println(new ObjectMapper().writeValueAsString(stringErrorDTO));
    }
}
