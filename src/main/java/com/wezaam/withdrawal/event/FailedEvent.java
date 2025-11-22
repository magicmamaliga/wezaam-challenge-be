package com.wezaam.withdrawal.event;

import com.wezaam.withdrawal.withdrawal.WithdrawalScheduled;

import jakarta.persistence.*;

@Entity(name = "failed_events")
public class FailedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private WithdrawalScheduled withdrawalScheduled;

    private String exception;

    public FailedEvent(Long id, WithdrawalScheduled withdrawalScheduled, String exception) {
        this.id = id;
        this.withdrawalScheduled = withdrawalScheduled;
        this.exception = exception;
    }

    public FailedEvent() {
    }
}