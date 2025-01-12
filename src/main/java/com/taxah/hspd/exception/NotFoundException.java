package com.taxah.hspd.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String notFoundMessage) {
        super(notFoundMessage);
    }
}
