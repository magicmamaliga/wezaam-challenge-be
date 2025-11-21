package com.wezaam.withdrawal.event;

import com.wezaam.withdrawal.withdrawal.WithdrawalScheduled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class EventsService {

    @Resource
    private final FailedEventRepository failedEventRepository;

    public EventsService(FailedEventRepository failedEventRepository) {
        this.failedEventRepository = failedEventRepository;
    }

    @Async
    @Retryable(value = Exception.class)
    public void send(WithdrawalScheduled withdrawal) {
        log.info("Entering send with withdrawal: {}", withdrawal);
        // build and send an event in message queue async
    }

    @Recover
    public void recover(Exception e, WithdrawalScheduled withdrawal) {
        log.error("Failed to send withdrawal event after retries: {}", withdrawal, e);
        failedEventRepository.save(new FailedEvent(null, withdrawal, e.getMessage()));
    }

}
