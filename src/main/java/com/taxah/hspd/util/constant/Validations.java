package com.taxah.hspd.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Validations {
    public static final String CANNOT_BE_NULL = " не может быть null";
    public static final String CANNOT_BE_EMPTY = " не может быть пустым";
    public static final String MUST_BE_NOT_LATER_THAN_PRESENT_TIME = " должен быть не позже настоящего времени";
    public static final String TICKER = "Ticker";
    public static final String START = "Start";
    public static final String END = "End";
    public static final String PASSWORD = "Пароль";
    public static final String TICKER_CANNOT_BE_NULL = TICKER + CANNOT_BE_NULL;
    public static final String TICKER_CANNOT_BE_EMPTY = TICKER + CANNOT_BE_EMPTY;
    public static final String START_MUST_BE_EARLIER_END_DATE = "Start должен быть раньше End даты";
    public static final String START_CANNOT_BE_NULL = START + CANNOT_BE_NULL;
    public static final String END_CANNOT_BE_NULL = END + CANNOT_BE_NULL;
    public static final String START_MUST_BE_NOT_LATER_THAN_PRESENT_TIME = START + MUST_BE_NOT_LATER_THAN_PRESENT_TIME;
    public static final String END_MUST_BE_NOT_LATER_THAN_PRESENT_TIME = END + MUST_BE_NOT_LATER_THAN_PRESENT_TIME;

    public static final int USERNAME_MIN_LENGTH = 3;
    public static final int USERNAME_MAX_LENGTH = 50;
    public static final String SYMBOLS_ENDING = " символов";
    public static final String USERNAME_MUST_CONTAIN_CHARACTERS = "Имя пользователя должно содержать от " +
            USERNAME_MIN_LENGTH + " до " + USERNAME_MAX_LENGTH + SYMBOLS_ENDING;
    public static final String USERNAME_CANNOT_BE_EMPTY = "Имя пользователя" + CANNOT_BE_EMPTY;

    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 60;
    public static final String PASSWORD_MUST_CONTAIN_CHARACTERS = "Длина пароля должна быть от " +
            PASSWORD_MIN_LENGTH + " до " + PASSWORD_MAX_LENGTH + SYMBOLS_ENDING;
    public static final String PASSWORD_CANNOT_BE_BLANK = PASSWORD + CANNOT_BE_EMPTY;

    public static final int EMAIL_MIN_LENGTH = 7;
    public static final int EMAIL_MAX_LENGTH = 70;
    public static final String EMAIL_MUST_CONTAIN_CHARACTERS = "Адрес электронной почты должен содержать от " +
            EMAIL_MIN_LENGTH + " до " + EMAIL_MAX_LENGTH + SYMBOLS_ENDING;
    public static final String EMAIL_ADDRESS_CANNOT_BE_EMPTY = "Адрес электронной почты" + CANNOT_BE_EMPTY;
    public static final String EMAIL_ADDRESS_MUST_BE_IN_FORMAT = "Email адрес должен быть в формате user@example.com";

    public static final String USERNAME_REGEX = "^[a-zA-Z][a-zA-Z0-9._-]{2,19}$";
    public static final String USERNAME_VALIDATION = "Имя пользователя должно начинаться с буквы и состоять из " +
            USERNAME_MIN_LENGTH + "-" + USERNAME_MAX_LENGTH +
            " символов, может содержать только буквы, цифры и «.», «_» и «-».";
}
