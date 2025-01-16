package com.taxah.hspd.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String APP_NAME = "hspd";
    public static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";
}
