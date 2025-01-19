package com.taxah.hspd.controller.handler;

import com.taxah.hspd.util.constant.Exceptions;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public interface UserAccessHandler {
    default String getAuthenticationUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException(Exceptions.USER_IS_NOT_AUTHENTICATED);
        }
        return authentication.getName();
    }
}
