package com.wezaam.withdrawal.withdrawal.dto;

import com.wezaam.withdrawal.withdrawal.WithdrawalScheduled;
import com.wezaam.withdrawal.withdrawal.WithdrawalStatus;
import com.wezaam.withdrawal.withdrawal.legacy.Withdrawal;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class WithdrawalFactory {

    public static WithdrawalScheduled createFromRequest(WithdrawalRequestDTO withdrawalRequestDTO) {
        return new WithdrawalScheduled(
                withdrawalRequestDTO.amount().doubleValue(),
                "ASAP".equals(withdrawalRequestDTO.executeAt()) ? Instant.now() :
                        Instant.parse(withdrawalRequestDTO.executeAt()),
                withdrawalRequestDTO.userId(),
                withdrawalRequestDTO.paymentMethodId(),
                WithdrawalStatus.PENDING);
    }

    public static List<WithdrawalDTO> createWithdrawalDTOsFromWithdrawalSchedules(List<WithdrawalScheduled> withdrawals) {
        return withdrawals.stream().map(WithdrawalFactory::createWithdrawalDTOFromWithdrawal).collect(toList());
    }

    public static List<WithdrawalDTO> createWithdrawalDTOsFromWithdrawals(List<Withdrawal> withdrawals) {
        return withdrawals.stream().map(
                withdrawal -> new WithdrawalDTO(withdrawal.getId(),
                        withdrawal.getTransactionId(),
                        withdrawal.getAmount(),
                        withdrawal.getCreatedAt(),
                        withdrawal.getUserId(),
                        withdrawal.getPaymentMethodId(),
                        withdrawal.getStatus(),
                        null)
        ).collect(toList());
    }

    public static WithdrawalDTO createWithdrawalDTOFromWithdrawal(WithdrawalScheduled withdrawalScheduled) {
        return new WithdrawalDTO(withdrawalScheduled.getId(),
                withdrawalScheduled.getTransactionId(),
                withdrawalScheduled.getAmount(),
                withdrawalScheduled.getCreatedAt(),
                withdrawalScheduled.getUserId(),
                withdrawalScheduled.getPaymentMethodId(),
                withdrawalScheduled.getStatus(),
                withdrawalScheduled.getExecuteAt());
    }
}
