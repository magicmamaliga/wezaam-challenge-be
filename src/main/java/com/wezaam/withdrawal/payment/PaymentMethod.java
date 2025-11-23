package com.wezaam.withdrawal.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wezaam.withdrawal.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.ToString;

import static jakarta.persistence.GenerationType.IDENTITY;

@ToString
@Getter
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
