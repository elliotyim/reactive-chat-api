package com.elldev.reactivechat.exception;

public class UserSessionNotFoundException extends Exception {
    public Integer errorCode;
    public String message;

    public UserSessionNotFoundException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}
