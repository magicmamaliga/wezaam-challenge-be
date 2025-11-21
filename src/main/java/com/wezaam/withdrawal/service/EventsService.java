package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.withdrawal.legacy.Withdrawal;
import com.wezaam.withdrawal.withdrawal.WithdrawalScheduled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventsService {

    @Async
    public void send(Withdrawal withdrawal) {
        // build and send an event in message queue async
    }

    @Async
    @Retryable(
            value = Exception.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000)
    )
    public void send(WithdrawalScheduled withdrawal) {
        // build and send an event in message queue async
    }

    @Recover
    public void recover(Exception e, Withdrawal withdrawal) {
        log.error("Failed to send withdrawal event after retries: {}", withdrawal, e);
        // Optional: log to DB, move to dead-letter queue, alert etc.
    }

}
