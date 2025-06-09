package com.bank.exception;

public class IllegalArgumentException extends RuntimeException {
    private String message;

    public IllegalArgumentException(String message) {
      super(message);
      this.message = message;
    }

    @Override
    public String getMessage() {
      return message;
    }
}
