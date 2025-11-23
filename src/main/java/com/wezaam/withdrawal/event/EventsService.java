package com.wezaam.withdrawal.event;

import com.wezaam.withdrawal.withdrawal.WithdrawalScheduled;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service responsible for sending events related to withdrawal operations.
 * <p>
 * Events are sent asynchronously and utilize a retry mechanism to ensure
 * reliable delivery. When event publishing ultimately fails after retries,
 * the failed event is persisted for further investigation.
 * </p>
 */
@Slf4j
@Service
public class EventsService {

    @Resource
    private FailedEventRepository failedEventRepository;

    /**
     * Sends an event associated with a scheduled withdrawal.
     * <p>
     * This operation runs asynchronously and will be retried automatically
     * if any exception occurs during execution. 3 times every 1000ms
     * </p>
     *
     * @param withdrawal the scheduled withdrawal for which an event should be sent
     */
    @Async
    @Retryable
    public void send(WithdrawalScheduled withdrawal) {
        log.info("Entering send with withdrawal: {}", withdrawal);
        // build and send an event in message queue async
    }

    /**
     * Callback method executed when the send operation repeatedly fails after retries.
     * <p>
     * Logs the error and saves the failed event into persistence for follow-up actions.
     * </p>
     *
     * @param e          the exception thrown during event processing
     * @param withdrawal the withdrawal linked to the failed event
     */
    @Recover
    public void recover(Exception e, WithdrawalScheduled withdrawal) {
        log.error("Failed to send withdrawal event after retries: {}", withdrawal, e);
        failedEventRepository.save(new FailedEvent(null, withdrawal, e.getMessage()));
    }

}
