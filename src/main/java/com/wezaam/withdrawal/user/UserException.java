package com.wezaam.withdrawal.user;

import com.wezaam.withdrawal.config.BaseBadRequestException;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}
