package com.wezaam.withdrawal.payment;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentMethodServiceTest {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @InjectMocks
    private PaymentMethodService paymentMethodService;

    @Test
    void findById_shouldReturnPaymentMethod_whenExists() {
        // Arrange
        Long id = 1L;
        PaymentMethod expectedMethod = new PaymentMethod();
        when(paymentMethodRepository.findById(id)).thenReturn(Optional.of(expectedMethod));

        // Act
        PaymentMethod result = paymentMethodService.findById(id);

        // Assert
        assertThat(result).isEqualTo(expectedMethod);
    }

    @Test
    void findById_shouldThrowPaymentMethodException_whenNotFound() {
        // Arrange
        Long id = 1L;
        when(paymentMethodRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> paymentMethodService.findById(id))
                .isInstanceOf(PaymentMethodException.class)
                .hasMessage("Payment method not found");
    }
}
