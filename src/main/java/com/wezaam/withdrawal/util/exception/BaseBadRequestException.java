package com.wezaam.withdrawal.util.exception;

public class BaseBadRequestException extends RuntimeException {
    public BaseBadRequestException(String message) {
        super(message);
    }
}
