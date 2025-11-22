package com.wezaam.withdrawal.withdrawal;

import com.wezaam.withdrawal.payment.PaymentMethodException;
import com.wezaam.withdrawal.payment.PaymentMethodService;
import com.wezaam.withdrawal.user.UserBadRequestException;
import com.wezaam.withdrawal.user.UserService;
import com.wezaam.withdrawal.withdrawal.dto.WithdrawalDTO;
import com.wezaam.withdrawal.withdrawal.dto.WithdrawalRequestDTO;
import com.wezaam.withdrawal.withdrawal.legacy.WithdrawalLegacyService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.wezaam.withdrawal.withdrawal.dto.WithdrawalFactory.*;

/**
 * Service responsible for managing withdrawal operations.
 * <p>
 * This class handles scheduling new withdrawal requests, retrieving existing withdrawals
 * (including legacy and scheduled ones), and processing withdrawals due for execution.
 * It coordinates user validation, payment method validation, transaction processing,
 * and event notifications.
 * </p>
 */
@Slf4j
@Service
public class WithdrawalService {

    @Resource
    private WithdrawalScheduledRepository withdrawalScheduledRepository;
    @Resource
    private PaymentMethodService paymentMethodService;
    @Resource
    private UserService userService;
    @Resource
    private WithdrawalLegacyService withdrawalLegacyService;

    /**
     * Schedules a withdrawal request for processing.
     * <p>
     * Validates that the user and payment method exist before persisting
     * the withdrawal request. The created withdrawal record will be marked
     * for execution at the scheduled time.
     * </p>
     *
     * @param withdrawalRequestDTO a DTO containing withdrawal request details,
     *                             including user ID, payment method ID, amount, and schedule time
     * @return {@link WithdrawalDTO} representing the newly scheduled withdrawal
     * @throws UserBadRequestException if the specified user does not exist
     * @throws PaymentMethodException  if the payment method is invalid
     */
    @Transactional
    public WithdrawalDTO schedule(WithdrawalRequestDTO withdrawalRequestDTO) {
        log.info("Entering schedule with withdrawalRequestDTO: {}", withdrawalRequestDTO);
        userService.findById(withdrawalRequestDTO.userId());
        paymentMethodService.findById(withdrawalRequestDTO.paymentMethodId());
        WithdrawalScheduled withdrawalScheduled =
                withdrawalScheduledRepository.save(createFromRequest(withdrawalRequestDTO));
        return createWithdrawalDTOFromWithdrawal(withdrawalScheduled);
    }


    /**
     * Retrieves all withdrawal records, including both legacy and scheduled withdrawals.
     *
     * @return a list of {@link WithdrawalDTO} representing all withdrawals
     */
    public List<WithdrawalDTO> findAll() {
        log.info("Entering findAll()");
        List<WithdrawalDTO> withdrawalDTOs = new ArrayList<>();
        withdrawalDTOs.addAll(withdrawalLegacyService.findAll());
        withdrawalDTOs.addAll(createWithdrawalDTOsFromWithdrawalSchedules(withdrawalScheduledRepository.findAll()));
        return withdrawalDTOs;
    }

}
