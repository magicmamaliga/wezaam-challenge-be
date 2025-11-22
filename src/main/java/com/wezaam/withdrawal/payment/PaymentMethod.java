package com.wezaam.withdrawal.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wezaam.withdrawal.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "payment_methods")
@Getter
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    private User user;
    private String name;

}
