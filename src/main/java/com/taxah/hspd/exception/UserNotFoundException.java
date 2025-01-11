package com.taxah.hspd.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String userNotFoundMessage) {
        super(userNotFoundMessage);
    }
}
