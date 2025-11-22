package com.wezaam.withdrawal.withdrawal;

import com.wezaam.withdrawal.event.EventsService;
import com.wezaam.withdrawal.payment.PaymentMethod;
import com.wezaam.withdrawal.payment.PaymentMethodException;
import com.wezaam.withdrawal.payment.PaymentMethodService;
import com.wezaam.withdrawal.transaction.TransactionException;
import com.wezaam.withdrawal.transaction.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static com.wezaam.withdrawal.withdrawal.WithdrawalStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WithdrawalProcessorTest {

    @Mock
    private WithdrawalScheduledRepository withdrawalScheduledRepository;

    @Mock
    private PaymentMethodService paymentMethodService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private EventsService eventsService;

    @InjectMocks
    private WithdrawalProcessor withdrawalProcessor;

    @Test
    void processScheduled_shouldMarkAsProcessing_andSendEvent_onSuccess() throws Exception {
        // Given
        WithdrawalScheduled withdrawal = new WithdrawalScheduled(100.0, Instant.now(), 1L, 10L, PENDING);
        PaymentMethod paymentMethod = new PaymentMethod();

        when(paymentMethodService.findById(10L)).thenReturn(paymentMethod);
        when(transactionService.sendToProcessing(100.0, paymentMethod)).thenReturn(123L);

        // When
        withdrawalProcessor.processScheduled(withdrawal);

        // Then
        assertThat(withdrawal.getStatus()).isEqualTo(PROCESSING);
        assertThat(withdrawal.getTransactionId()).isEqualTo(123L);

        verify(eventsService).send(withdrawal);
        verify(withdrawalScheduledRepository).save(withdrawal);
    }

    @Test
    void processScheduled_shouldSkip_whenPaymentMethodException() throws TransactionException {
        // Given
        WithdrawalScheduled withdrawal = new WithdrawalScheduled(100.0, Instant.now(), 1L, 10L, PENDING);

        when(paymentMethodService.findById(10L))
                .thenThrow(new PaymentMethodException("Payment method not found"));

        // When
        withdrawalProcessor.processScheduled(withdrawal);

        // Then
        assertThat(withdrawal.getStatus()).isEqualTo(PENDING); // unchanged

        verify(eventsService, never()).send(any());
        verify(withdrawalScheduledRepository, never()).save(any());
        verify(transactionService, never()).sendToProcessing(anyDouble(), any());
    }

    @Test
    void processScheduled_shouldSetFailed_whenTransactionException() throws Exception {
        // Given
        WithdrawalScheduled withdrawal = new WithdrawalScheduled(100.0, Instant.now(), 1L, 10L, PENDING);
        PaymentMethod paymentMethod = new PaymentMethod();

        when(paymentMethodService.findById(10L)).thenReturn(paymentMethod);
        when(transactionService.sendToProcessing(anyDouble(), any()))
                .thenThrow(new TransactionException("Transaction failed"));

        // When
        withdrawalProcessor.processScheduled(withdrawal);

        // Then
        assertThat(withdrawal.getStatus()).isEqualTo(FAILED);
        assertThat(withdrawal.getTransactionId()).isNull();

        verify(eventsService).send(withdrawal);
        verify(withdrawalScheduledRepository).save(withdrawal);
    }

    @Test
    void processScheduled_shouldSetInternalError_onUnexpectedException() throws Exception {
        // Given
        WithdrawalScheduled withdrawal = new WithdrawalScheduled(100.0, Instant.now(), 1L, 10L, PENDING);
        PaymentMethod paymentMethod = new PaymentMethod();

        when(paymentMethodService.findById(10L)).thenReturn(paymentMethod);
        when(transactionService.sendToProcessing(anyDouble(), any()))
                .thenThrow(new RuntimeException("Unexpected error"));

        // When
        withdrawalProcessor.processScheduled(withdrawal);

        // Then
        assertThat(withdrawal.getStatus()).isEqualTo(INTERNAL_ERROR);
        assertThat(withdrawal.getTransactionId()).isNull();

        verify(eventsService).send(withdrawal);
        verify(withdrawalScheduledRepository).save(withdrawal);
    }
}
