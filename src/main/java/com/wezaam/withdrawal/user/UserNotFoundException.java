package com.wezaam.withdrawal.user;

import com.wezaam.withdrawal.exception.BaseNotFoundException;

public class UserNotFoundException extends BaseNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
