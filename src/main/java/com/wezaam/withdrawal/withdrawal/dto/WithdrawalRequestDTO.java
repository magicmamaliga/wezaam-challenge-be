package com.wezaam.withdrawal.withdrawal.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public record WithdrawalRequestDTO(
        @NotNull Long userId,
        @NotNull Long paymentMethodId,
        @NotNull @Positive BigDecimal amount,
        @NotNull String executeAt
) {}