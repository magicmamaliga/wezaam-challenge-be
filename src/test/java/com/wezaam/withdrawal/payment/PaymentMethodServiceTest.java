package com.wezaam.withdrawal.payment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentMethodServiceTest {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @InjectMocks
    private PaymentMethodService paymentMethodService;

    @Test
    void findById_shouldReturnPaymentMethod_whenExists() {
        // Given
        Long id = 1L;
        PaymentMethod expectedMethod = new PaymentMethod();
        when(paymentMethodRepository.findById(id)).thenReturn(Optional.of(expectedMethod));

        // When
        PaymentMethod result = paymentMethodService.findById(id);

        // Then
        assertThat(result).isEqualTo(expectedMethod);
    }

    @Test
    void findById_shouldThrowPaymentMethodException_whenNotFound() {
        // Given
        Long id = 1L;
        when(paymentMethodRepository.findById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> paymentMethodService.findById(id))
                .isInstanceOf(PaymentMethodException.class)
                .hasMessage("Payment method not found");
    }
}
