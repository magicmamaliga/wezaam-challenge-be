package com.wezaam.withdrawal.event;

import com.wezaam.withdrawal.withdrawal.WithdrawalScheduled;

import javax.persistence.*;

@Entity(name = "failed_events")
public record FailedEvent(@Id @GeneratedValue(strategy = GenerationType.AUTO) Long id,
                          @OneToOne WithdrawalScheduled withdrawalScheduled,
                          String exception) {
}
