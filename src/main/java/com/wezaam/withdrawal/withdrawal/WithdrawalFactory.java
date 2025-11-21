package com.wezaam.withdrawal.withdrawal;

import com.wezaam.withdrawal.withdrawal.legacy.Withdrawal;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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
        return withdrawals.stream().map(WithdrawalFactory::createWithdrawalDTOFromWithdrawal).collect(Collectors.toList());
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
        ).collect(Collectors.toList());
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
