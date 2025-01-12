package com.taxah.hspd.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String alreadyExistsMessage) {
        super(alreadyExistsMessage);
    }
}
