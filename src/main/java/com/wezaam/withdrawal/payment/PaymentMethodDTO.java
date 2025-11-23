package com.wezaam.withdrawal.payment;

//This may be needed if transaction would want to send PaymentMethod
public record PaymentMethodDTO(Long id, Long userId, String name) {
}
