package com.wezaam.withdrawal.withdrawal;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Entity(name = "scheduled_withdrawals")
public class WithdrawalScheduled {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Setter
    private Long transactionId;
    private Double amount;
    private Instant createdAt;
    private Instant executeAt;
    private Long userId;
    private Long paymentMethodId;
    @Setter
    @Enumerated(EnumType.STRING)
    private WithdrawalStatus status;

    public WithdrawalScheduled(Double amount, Instant executeAt, Long userId, Long paymentMethodId,
                               WithdrawalStatus status) {
        this.amount = amount;
        this.executeAt = executeAt;
        this.userId = userId;
        this.paymentMethodId = paymentMethodId;
        this.status = status;
        this.createdAt = Instant.now();
    }

    public WithdrawalScheduled() {
    }

}
