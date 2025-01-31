package com.taxah.hspd.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtRefreshExpiredException extends AuthenticationException {
    public JwtRefreshExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
