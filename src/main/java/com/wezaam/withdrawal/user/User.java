package com.wezaam.withdrawal.user;

import com.wezaam.withdrawal.payment.PaymentMethod;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String firstName;
    @OneToMany(mappedBy="user")
    private List<PaymentMethod> paymentMethods;
    private Double maxWithdrawalAmount;

}
