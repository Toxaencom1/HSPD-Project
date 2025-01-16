package com.taxah.hspd.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Endpoints {
    public static final String API_USER = "/api/user";
    public static final String SAVE = "/save";
    public static final String SAVED = "/saved";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String API_USER_REGISTER = API_USER + REGISTER;
    public static final String API_USER_LOGIN = API_USER + LOGIN;
    public static final String SW_UI = "/swagger-ui/**";
    public static final String SW_RESOURCES = "/swagger-resources/*";
    public static final String SW_API_DOCS = "/v3/api-docs/**";
    public static final String PATH_VARIABLE_ID = "/{id}";
    public static final String TEST_INFO_ENDPOINT = "/api/info/test";
    public static final String TEST_INFO_PATH = "/api/info/**";
}
