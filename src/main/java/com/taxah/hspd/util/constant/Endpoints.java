package com.taxah.hspd.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Endpoints {
    public static final String APP_API_VERSION = "v1";
    public static final String API_USER = "/api/" + APP_API_VERSION + "/user";
    public static final String API_DATA = "/api/" + APP_API_VERSION + "/data";
    public static final String SAVE = "/save";
    public static final String FETCH = "/fetch";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String REFRESH = "/refresh";
    public static final String API_USER_REGISTER = API_USER + REGISTER;
    public static final String API_USER_LOGIN = API_USER + LOGIN;
    public static final String API_USER_REFRESH = API_USER + REFRESH;
    public static final String SW_UI = "/swagger-ui/**";
    public static final String SW_RESOURCES = "/swagger-resources/*";
    public static final String SW_API_DOCS = "/v3/api-docs/**";
    public static final String PATH_VARIABLE_ID = "/{id}";
    public static final String TEST_INFO_ENDPOINT = "/api/" + APP_API_VERSION + "/info/test";
    public static final String TEST_INFO_PATH = "/api/" + APP_API_VERSION + "/info/**";
}
