package com.wezaam.withdrawal.withdrawal;

import com.wezaam.withdrawal.payment.PaymentMethod;
import com.wezaam.withdrawal.payment.PaymentMethodException;
import com.wezaam.withdrawal.payment.PaymentMethodService;
import com.wezaam.withdrawal.event.EventsService;
import com.wezaam.withdrawal.transaction.TransactionException;
import com.wezaam.withdrawal.transaction.TransactionService;
import com.wezaam.withdrawal.user.UserService;
import com.wezaam.withdrawal.withdrawal.dto.WithdrawalDTO;
import com.wezaam.withdrawal.withdrawal.dto.WithdrawalRequestDTO;
import com.wezaam.withdrawal.withdrawal.legacy.WithdrawalLegacyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import java.time.Instant;
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
    private TransactionService transactionService;
    @Resource
    private EventsService eventsService;
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
     * @throws com.wezaam.withdrawal.user.UserException if the specified user does not exist
     * @throws com.wezaam.withdrawal.payment.PaymentMethodException if the payment method is invalid
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

    /**
     * Scheduled task executed every 5 seconds.
     * <p>
     * Finds all withdrawals that are scheduled for execution before the current time
     * and processes them sequentially.
     * </p>
     */
    @Scheduled(fixedDelay = 5000)
    public void run() {
        log.info("Process scheduled withdrawals");
        withdrawalScheduledRepository.findAllByExecuteAtBefore(Instant.now()).forEach(this::processScheduled);
    }

    /**
     * Processes a scheduled withdrawal.
     * <p>
     * The method attempts to retrieve the associated payment method and then
     * initiates transaction processing. Based on the outcome, the withdrawal
     * status is updated accordingly:
     * <ul>
     *   <li>{@code PROCESSING} if successfully forwarded for processing</li>
     *   <li>{@code FAILED} if transaction fails</li>
     *   <li>{@code INTERNAL_ERROR} for unexpected errors</li>
     * </ul>
     * After processing, an event notification is sent and the updated withdrawal is persisted.
     * </p>
     *
     * @param withdrawal the scheduled withdrawal entity to process
     */
    void processScheduled(WithdrawalScheduled withdrawal) {

        PaymentMethod paymentMethod;
        try {
            paymentMethod = paymentMethodService.findById(withdrawal.getPaymentMethodId());
        } catch (PaymentMethodException e) {
            log.error("PaymentMethod not found, will retry in the next run, for withdrawal: {}, errorMessage: {}",
                    withdrawal, e.getMessage());
            return;
        }

        try {
            var transactionId = transactionService.sendToProcessing(withdrawal.getAmount(), paymentMethod);
            withdrawal.setStatus(WithdrawalStatus.PROCESSING);
            withdrawal.setTransactionId(transactionId);
        } catch (TransactionException e) {
            withdrawal.setStatus(WithdrawalStatus.FAILED);
        } catch (Exception e) {
            withdrawal.setStatus(WithdrawalStatus.INTERNAL_ERROR);
        }
        eventsService.send(withdrawal);
        withdrawalScheduledRepository.save(withdrawal);
    }

}
