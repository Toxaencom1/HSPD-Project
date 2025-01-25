package com.taxah.hspd.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Security {
    public static final String ANONYMOUS_USERNAME = "anonymousUser";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    public static final String ADD_ROLE_AUTHORITY = "hasAuthority('admin_permission')";
    public static final String USER_AUTHORITY = "hasAuthority('user_permission')";
}
