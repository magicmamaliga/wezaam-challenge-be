package com.wezaam.withdrawal.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class PaymentMethodServiceIntegrationTest {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Test
    void findById_shouldReturnExistingPaymentMethod() {
        PaymentMethod method = paymentMethodService.findById(1L);

        assertThat(method).isNotNull();
        assertThat(method.getName()).isEqualTo("My bank account");
        assertThat(method.getUser().getId()).isEqualTo(1L);
    }

    @Test
    void findById_shouldThrowException_whenPaymentMethodNotFound() {
        assertThatThrownBy(() -> paymentMethodService.findById(-1L))
                .isInstanceOf(PaymentMethodException.class)
                .hasMessage("Payment method not found");
    }
}

