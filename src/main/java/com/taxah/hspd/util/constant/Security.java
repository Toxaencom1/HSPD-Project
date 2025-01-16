package com.taxah.hspd.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Security {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    public static final String ADD_ROLE_AUTHORITY = "hasAuthority('post_user_permission')";
    public static final String USER_AUTHORITY = "hasAuthority('get_user_permission')";
}
