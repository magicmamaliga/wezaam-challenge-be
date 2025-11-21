package com.wezaam.withdrawal.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wezaam.withdrawal.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "payment_methods")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    private User user;
    private String name;

}
