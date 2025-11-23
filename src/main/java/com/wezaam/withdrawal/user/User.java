package com.wezaam.withdrawal.user;

import com.wezaam.withdrawal.payment.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "users")
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @OneToMany(mappedBy = "user")
    private List<PaymentMethod> paymentMethods;
    @Column(name = "max_withdrawal_amount")
    private Double maxWithdrawalAmount;

    public User() {
    }

    User(Long id, String firstName) {
        this.id = id;
        this.firstName = firstName;
    }

    User(Long id, String firstName, Double maxWithdrawalAmount) {
        this.id = id;
        this.firstName = firstName;
        this.maxWithdrawalAmount = maxWithdrawalAmount;
    }
}
