package com.wezaam.withdrawal.processor;

import com.wezaam.withdrawal.processor.event.EventsService;
import com.wezaam.withdrawal.payment.PaymentMethod;
import com.wezaam.withdrawal.payment.PaymentMethodException;
import com.wezaam.withdrawal.payment.PaymentMethodService;
import com.wezaam.withdrawal.processor.transaction.TransactionException;
import com.wezaam.withdrawal.processor.transaction.TransactionService;
import com.wezaam.withdrawal.withdrawal.WithdrawalScheduled;
import com.wezaam.withdrawal.withdrawal.WithdrawalScheduledRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static com.wezaam.withdrawal.withdrawal.WithdrawalStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WithdrawalProcessorIntegrationTest {

    @Autowired
    private WithdrawalScheduledRepository withdrawalScheduledRepository;

    @Autowired
    private WithdrawalProcessor withdrawalProcessor;

    @MockitoBean
    private PaymentMethodService paymentMethodService;

    @MockitoBean
    private TransactionService transactionService;

    @MockitoBean
    private EventsService eventsService;

    private WithdrawalScheduled withdrawal;

    @BeforeEach
    void setup() {
        withdrawal = new WithdrawalScheduled(
                100.0,
                Instant.now().minusSeconds(10),  // Ready to run
                1L,
                10L,
                PENDING
        );
        withdrawal = withdrawalScheduledRepository.save(withdrawal);
    }

    @Test
    void run_shouldProcessWithdrawalSuccessfully() throws Exception {
        // Given
        PaymentMethod paymentMethod = new PaymentMethod();
        when(paymentMethodService.findById(10L)).thenReturn(paymentMethod);
        when(transactionService.sendToProcessing(100.0, paymentMethod)).thenReturn(123L);

        // When
        withdrawalProcessor.run();

        // Then
        WithdrawalScheduled result = withdrawalScheduledRepository.findById(withdrawal.getId()).orElseThrow();

        assertThat(result.getStatus()).isEqualTo(PROCESSING);
        assertThat(result.getTransactionId()).isEqualTo(123L);
        verify(eventsService).send(result);
    }

    @Test
    void run_shouldSkip_whenPaymentMethodException() throws TransactionException {
        // Given
        when(paymentMethodService.findById(10L)).thenThrow(new PaymentMethodException("Not found"));

        // When
        withdrawalProcessor.run();

        // Then
        WithdrawalScheduled result = withdrawalScheduledRepository.findById(withdrawal.getId()).orElseThrow();
        assertThat(result.getStatus()).isEqualTo(PENDING);  // unchanged
        verify(eventsService, never()).send(any());
        verify(transactionService, never()).sendToProcessing(anyDouble(), any());
    }

    @Test
    void run_shouldSetFailed_whenTransactionException() throws Exception {
        // Given
        PaymentMethod paymentMethod = new PaymentMethod();
        when(paymentMethodService.findById(10L)).thenReturn(paymentMethod);
        when(transactionService.sendToProcessing(anyDouble(), any())).thenThrow(new TransactionException("Error"));

        // When
        withdrawalProcessor.run();

        // Then
        WithdrawalScheduled result = withdrawalScheduledRepository.findById(withdrawal.getId()).orElseThrow();
        assertThat(result.getStatus()).isEqualTo(FAILED);
        verify(eventsService).send(result);
    }

    @Test
    void run_shouldSetInternalError_whenUnexpectedException() throws Exception {
        // Given
        PaymentMethod paymentMethod = new PaymentMethod();
        when(paymentMethodService.findById(10L)).thenReturn(paymentMethod);
        when(transactionService.sendToProcessing(anyDouble(), any())).thenThrow(new RuntimeException("DB down"));

        // When
        withdrawalProcessor.run();

        // Then
        WithdrawalScheduled result = withdrawalScheduledRepository.findById(withdrawal.getId()).orElseThrow();
        assertThat(result.getStatus()).isEqualTo(INTERNAL_ERROR);
        verify(eventsService).send(result);
    }
}
