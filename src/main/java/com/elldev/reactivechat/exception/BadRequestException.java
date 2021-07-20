package com.elldev.reactivechat.exception;

public class BadRequestException extends Exception {
    public Integer errorCode;
    public String message;

    public BadRequestException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}
