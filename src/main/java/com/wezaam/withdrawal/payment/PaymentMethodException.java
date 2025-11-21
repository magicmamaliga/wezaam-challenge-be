package com.wezaam.withdrawal.payment;

public class PaymentMethodException extends RuntimeException {
    public PaymentMethodException(String message) {
        super(message);
    }
}
