package com.wezaam.withdrawal.user;

import com.wezaam.withdrawal.exception.BaseBadRequestException;

public class UserBadRequestException extends BaseBadRequestException {
    public UserBadRequestException(String message) {
        super(message);
    }
}
