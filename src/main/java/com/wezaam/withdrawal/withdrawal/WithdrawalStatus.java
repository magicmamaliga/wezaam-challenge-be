package com.wezaam.withdrawal.withdrawal;

//Todo: check if the enum propagates to db as number or its name.
public enum WithdrawalStatus {

    PENDING,
    SUCCESS,
    PROCESSING,
    INTERNAL_ERROR,
    FAILED
}
