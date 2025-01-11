package com.taxah.hspd.exception;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(String roleNotFoundMessage) {
        super(roleNotFoundMessage);
    }
}
