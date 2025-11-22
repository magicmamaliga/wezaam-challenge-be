package com.wezaam.withdrawal.withdrawal.dto;

import com.wezaam.withdrawal.withdrawal.WithdrawalBadRequestException;
import com.wezaam.withdrawal.withdrawal.WithdrawalScheduled;
import com.wezaam.withdrawal.withdrawal.WithdrawalStatus;
import com.wezaam.withdrawal.withdrawal.legacy.Withdrawal;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class WithdrawalFactory {

    public static final String ASAP = "ASAP";

    public static WithdrawalScheduled createFromRequest(WithdrawalRequestDTO withdrawalRequestDTO) {

        Instant executeAt;
        if (ASAP.equalsIgnoreCase(withdrawalRequestDTO.executeAt())) {
            executeAt = Instant.now();
        } else {
            try {
                executeAt = Instant.parse(withdrawalRequestDTO.executeAt());
            } catch (DateTimeParseException ex) {
                throw new WithdrawalBadRequestException("Invalid executeAt timestamp: " + withdrawalRequestDTO.executeAt(), ex);
            }
        }

        return new WithdrawalScheduled(withdrawalRequestDTO.amount().doubleValue(), executeAt,
                withdrawalRequestDTO.userId(), withdrawalRequestDTO.paymentMethodId(), WithdrawalStatus.PENDING);
    }

    public static List<WithdrawalDTO> createWithdrawalDTOsFromWithdrawalSchedules(List<WithdrawalScheduled> withdrawals) {
        return withdrawals.stream().map(WithdrawalFactory::createWithdrawalDTOFromWithdrawal).collect(toList());
    }

    public static List<WithdrawalDTO> createWithdrawalDTOsFromWithdrawals(List<Withdrawal> withdrawals) {
        return withdrawals.stream().map(withdrawal -> new WithdrawalDTO(withdrawal.getId(),
                withdrawal.getTransactionId(), withdrawal.getAmount(), withdrawal.getCreatedAt(),
                withdrawal.getUserId(), withdrawal.getPaymentMethodId(), withdrawal.getStatus(), null)).collect(toList());
    }

    public static WithdrawalDTO createWithdrawalDTOFromWithdrawal(WithdrawalScheduled withdrawalScheduled) {
        return new WithdrawalDTO(withdrawalScheduled.getId(), withdrawalScheduled.getTransactionId(),
                withdrawalScheduled.getAmount(), withdrawalScheduled.getCreatedAt(), withdrawalScheduled.getUserId(),
                withdrawalScheduled.getPaymentMethodId(), withdrawalScheduled.getStatus(),
                withdrawalScheduled.getExecuteAt());
    }
}
