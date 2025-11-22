package com.wezaam.withdrawal.withdrawal.legacy;

import com.wezaam.withdrawal.withdrawal.WithdrawalStatus;
import lombok.Getter;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "withdrawals")
@Getter
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long transactionId;
    private Double amount;
    private Instant createdAt;
    private Long userId;
    private Long paymentMethodId;
    @Enumerated(EnumType.STRING)
    private WithdrawalStatus status;

    public Withdrawal() {
    }

    public Withdrawal(Long id, Long transactionId, Double amount, Instant createdAt, Long userId, Long paymentMethodId, WithdrawalStatus status) {
        this.id = id;
        this.transactionId = transactionId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.userId = userId;
        this.paymentMethodId = paymentMethodId;
        this.status = status;
    }
}
