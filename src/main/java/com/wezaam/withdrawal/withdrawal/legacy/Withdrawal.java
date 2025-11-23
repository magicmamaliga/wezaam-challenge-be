package com.wezaam.withdrawal.withdrawal.legacy;

import com.wezaam.withdrawal.withdrawal.WithdrawalStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;

@ToString
@Getter
@Entity(name = "withdrawals")
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

    public Withdrawal(Long id, Long transactionId, Double amount, Instant createdAt, Long userId,
                      Long paymentMethodId, WithdrawalStatus status) {
        this.id = id;
        this.transactionId = transactionId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.userId = userId;
        this.paymentMethodId = paymentMethodId;
        this.status = status;
    }
}
