package com.wezaam.withdrawal.withdrawal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@ToString
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
