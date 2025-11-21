package com.wezaam.withdrawal.withdrawal;

import java.time.Instant;

public record WithdrawalDTO(Long id, Long transactionId, Double amount, Instant createdAt, Long userId,
                            Long paymentMethodId, WithdrawalStatus status, Instant executeAt) {
}
