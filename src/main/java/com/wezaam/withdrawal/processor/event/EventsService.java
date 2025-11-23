package com.wezaam.withdrawal.processor.event;

import com.wezaam.withdrawal.withdrawal.WithdrawalScheduled;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
    private EventServiceRetry eventServiceRetry;

    /**
     * Sends an event associated with a scheduled withdrawal.
     *
     * @param withdrawal the scheduled withdrawal for which an event should be sent
     */
    @Async
    public void send(WithdrawalScheduled withdrawal) {
        log.info("Entering send with withdrawal: {}", withdrawal);
        // build and send an event in message queue async
        eventServiceRetry.sendWithRetries(withdrawal);
    }

}
