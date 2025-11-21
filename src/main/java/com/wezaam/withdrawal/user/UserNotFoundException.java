package com.wezaam.withdrawal.user;

import com.wezaam.withdrawal.config.BaseNotFoundException;

public class UserNotFoundException extends BaseNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
