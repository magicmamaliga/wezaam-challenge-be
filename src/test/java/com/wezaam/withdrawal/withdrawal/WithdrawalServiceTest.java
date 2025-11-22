package com.wezaam.withdrawal.withdrawal;

import com.wezaam.withdrawal.payment.PaymentMethodException;
import com.wezaam.withdrawal.payment.PaymentMethodService;
import com.wezaam.withdrawal.user.UserBadRequestException;
import com.wezaam.withdrawal.user.UserService;
import com.wezaam.withdrawal.withdrawal.dto.WithdrawalDTO;
import com.wezaam.withdrawal.withdrawal.dto.WithdrawalRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static com.wezaam.withdrawal.withdrawal.WithdrawalStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WithdrawalServiceTest {

    @Mock
    private WithdrawalScheduledRepository withdrawalScheduledRepository;
    @Mock
    private PaymentMethodService paymentMethodService;
    @Mock
    private UserService userService;
    @InjectMocks
    private WithdrawalService withdrawalService;

    @Test
    void schedule_shouldCreateWithdrawal_whenValidRequest() {
        // Given
        WithdrawalRequestDTO requestDTO =
                new WithdrawalRequestDTO(1L, 10L, BigDecimal.valueOf(100), "ASAP");

        WithdrawalScheduled savedWithdrawal = new WithdrawalScheduled(
                100.0, Instant.now(), 1L, 10L, PENDING
        );

        when(withdrawalScheduledRepository.save(any())).thenReturn(savedWithdrawal);

        // When
        WithdrawalDTO result = withdrawalService.schedule(requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.paymentMethodId()).isEqualTo(10L);
        assertThat(result.amount()).isEqualTo(100.0);
        assertThat(result.status()).isEqualTo(PENDING);

        verify(userService).findById(1L);
        verify(paymentMethodService).findById(10L);
        verify(withdrawalScheduledRepository).save(any());
    }

    @Test
    void schedule_shouldThrowException_whenUserNotFound() {
        // Given
        WithdrawalRequestDTO requestDTO =
                new WithdrawalRequestDTO(1L, 10L, BigDecimal.valueOf(100), "ASAP");

        doThrow(new UserBadRequestException("User not found"))
                .when(userService).findById(1L);

        // When / Then
        assertThatThrownBy(() -> withdrawalService.schedule(requestDTO))
                .isInstanceOf(UserBadRequestException.class)
                .hasMessage("User not found");

        verify(paymentMethodService, never()).findById(any());
        verify(withdrawalScheduledRepository, never()).save(any());
    }

    @Test
    void schedule_shouldThrowException_whenPaymentMethodNotFound() {
        // Given
        WithdrawalRequestDTO requestDTO =
                new WithdrawalRequestDTO(1L, 10L, BigDecimal.valueOf(100), "ASAP");

        doThrow(new PaymentMethodException("Payment method not found"))
                .when(paymentMethodService).findById(10L);

        // When / Then
        assertThatThrownBy(() -> withdrawalService.schedule(requestDTO))
                .isInstanceOf(PaymentMethodException.class)
                .hasMessage("Payment method not found");

        verify(withdrawalScheduledRepository, never()).save(any());
    }

}
