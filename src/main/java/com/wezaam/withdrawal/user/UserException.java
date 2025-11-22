package com.wezaam.withdrawal.user;

import com.wezaam.withdrawal.exception.BaseBadRequestException;

public class UserException extends BaseBadRequestException {
    public UserException(String message) {
        super(message);
    }
}
