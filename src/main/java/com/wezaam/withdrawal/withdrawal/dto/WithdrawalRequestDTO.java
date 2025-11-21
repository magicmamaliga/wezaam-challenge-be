package com.wezaam.withdrawal.withdrawal.dto;

import com.sun.istack.NotNull;

import java.math.BigDecimal;

public record WithdrawalRequestDTO(
        @NotNull Long userId,
        @NotNull Long paymentMethodId,
        @NotNull BigDecimal amount,
        @NotNull String executeAt
) {}