package com.wezaam.withdrawal.withdrawal.dto;

import com.wezaam.withdrawal.withdrawal.WithdrawalStatus;

import java.time.Instant;

public record WithdrawalDTO(Long id, Long transactionId, Double amount, Instant createdAt, Long userId,
                            Long paymentMethodId, WithdrawalStatus status, Instant executeAt) {
}
