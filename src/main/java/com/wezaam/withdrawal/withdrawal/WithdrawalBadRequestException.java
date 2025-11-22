package com.wezaam.withdrawal.withdrawal;

public class WithdrawalBadRequestException extends RuntimeException {

    public WithdrawalBadRequestException(String message, Exception cause) {
        super(message, cause);
    }

}
