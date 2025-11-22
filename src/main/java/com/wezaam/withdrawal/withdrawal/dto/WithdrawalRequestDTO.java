package com.wezaam.withdrawal.withdrawal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record WithdrawalRequestDTO(
        @NotNull Long userId,
        @NotNull Long paymentMethodId,
        @NotNull @Positive BigDecimal amount,
        @NotNull String executeAt
) {}