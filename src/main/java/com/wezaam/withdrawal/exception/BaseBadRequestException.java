package com.wezaam.withdrawal.exception;

public class BaseBadRequestException extends RuntimeException {
    public BaseBadRequestException(String message) {
        super(message);
    }
}
