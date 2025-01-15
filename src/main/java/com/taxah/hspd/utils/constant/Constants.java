package com.taxah.hspd.utils.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

//    public static final String  = "";

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String APP_NAME = "hspd";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
}
