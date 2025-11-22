package com.wezaam.withdrawal.withdrawal;

import com.wezaam.withdrawal.event.EventsService;
import com.wezaam.withdrawal.payment.PaymentMethod;
import com.wezaam.withdrawal.payment.PaymentMethodException;
import com.wezaam.withdrawal.payment.PaymentMethodService;
import com.wezaam.withdrawal.transaction.TransactionException;
import com.wezaam.withdrawal.transaction.TransactionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Processes the scheduled withdrawals
 */
@Slf4j
@Service
public class WithdrawalProcessor {

    @Resource
    private WithdrawalScheduledRepository withdrawalScheduledRepository;
    @Resource
    private PaymentMethodService paymentMethodService;
    @Resource
    private TransactionService transactionService;
    @Resource
    private EventsService eventsService;

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
