package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.withdrawal.Withdrawal;
import com.wezaam.withdrawal.withdrawal.scheduled.WithdrawalScheduled;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EventsService {

    @Async
    public void send(Withdrawal withdrawal) {
        // build and send an event in message queue async
    }

    @Async
    public void send(WithdrawalScheduled withdrawal) {
        // build and send an event in message queue async
    }
}
