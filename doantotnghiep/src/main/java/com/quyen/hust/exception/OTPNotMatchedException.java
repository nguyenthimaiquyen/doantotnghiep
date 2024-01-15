package com.quyen.hust.exception;

public class OTPNotMatchedException extends Exception {
    public OTPNotMatchedException(String message) {
        super(message);
    }
}
