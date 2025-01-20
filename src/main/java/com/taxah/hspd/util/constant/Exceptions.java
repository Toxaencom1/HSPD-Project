package com.taxah.hspd.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Exceptions {
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_NOT_FOUND_F = "User '%s' not found";
    public static final String ROLE_NOT_FOUND_F = "Role %s not found";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String DATA_ALREADY_EXISTS_F = "Data already exists for %s or empty results for period %s and %s";
    public static final String NO_TICKER_FOUND_F = "No ticker found for %s";
    public static final String NO_SAVED_TICKER_FOUND_F = "No saved ticker found for %s";
    public static final String USER_IS_NOT_AUTHENTICATED = "User is not authenticated. Please log in.";
    public static final String NO_DATA_FOUND_F = "No data found in database for %s";
    public static final String NO_DATA_FOUND_IN_POLYGON_FOR_PERIOD_F = "No data found in Polygon.io for %s for period %s and %s";
    public static final String NO_API_RESULTS_FOUND = "No API results found";
    public static final String TICKER_NOT_FOUND_F = "Ticker not found for %s";
    public static final String UNSUPPORTED_TICKER_F = "Unsupported Ticker: %s";


}
