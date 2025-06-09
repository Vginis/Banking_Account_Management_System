package com.bank.exception;

public class UnauthorizedException extends RuntimeException{
    private String message;

    public UnauthorizedException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
