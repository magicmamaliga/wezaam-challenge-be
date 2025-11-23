package com.wezaam.withdrawal.withdrawal.dto;

import com.wezaam.withdrawal.withdrawal.WithdrawalBadRequestException;
import com.wezaam.withdrawal.withdrawal.WithdrawalScheduled;
import com.wezaam.withdrawal.withdrawal.WithdrawalStatus;
import com.wezaam.withdrawal.withdrawal.legacy.Withdrawal;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class WithdrawalFactoryTest {

    @Test
    void createFromRequest_shouldCreateWithdrawalScheduled_whenExecuteAtIsASAP() {
        // Given
        WithdrawalRequestDTO requestDTO = new WithdrawalRequestDTO(1L, 10L, BigDecimal.valueOf(100), "ASAP");

        // When
        WithdrawalScheduled result = WithdrawalFactory.createFromRequest(requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(100.0);
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getPaymentMethodId()).isEqualTo(10L);
        assertThat(result.getStatus()).isEqualTo(WithdrawalStatus.PENDING);
        assertThat(result.getExecuteAt()).isNotNull().isCloseTo(Instant.now(), within(1, ChronoUnit.SECONDS));
    }

    @Test
    void createFromRequest_shouldUseParsedTimestamp_whenExecuteAtIsValidISO() {
        // Given
        String timestamp = "2025-01-01T10:00:00Z";
        WithdrawalRequestDTO requestDTO = new WithdrawalRequestDTO(1L, 10L, BigDecimal.valueOf(100), timestamp);

        // When
        WithdrawalScheduled result = WithdrawalFactory.createFromRequest(requestDTO);

        // Then
        assertThat(result.getExecuteAt()).isEqualTo(Instant.parse(timestamp));
        assertThat(result.getStatus()).isEqualTo(WithdrawalStatus.PENDING);
    }

    @Test
    void createFromRequest_shouldThrowException_whenExecuteAtIsInvalid() {
        // Given
        WithdrawalRequestDTO requestDTO = new WithdrawalRequestDTO(1L, 10L, BigDecimal.valueOf(100), "Invalid " +
                "Timestamp");

        // When & Then
        assertThatThrownBy(() -> WithdrawalFactory.createFromRequest(requestDTO))
                .isInstanceOf(WithdrawalBadRequestException.class)
                .hasMessageContaining("Invalid executeAt timestamp");
    }

    @Test
    void createWithdrawalDTOFromScheduled_shouldMapCorrectly() {
        // Given
        WithdrawalScheduled scheduled = new WithdrawalScheduled(300.0, Instant.now(), 3L, 30L,
                WithdrawalStatus.PENDING);

        // When
        WithdrawalDTO dto = WithdrawalFactory.createWithdrawalDTOFromWithdrawal(scheduled);

        // Then
        assertThat(dto.amount()).isEqualTo(300.0);
        assertThat(dto.userId()).isEqualTo(3L);
        assertThat(dto.paymentMethodId()).isEqualTo(30L);
        assertThat(dto.status()).isEqualTo(WithdrawalStatus.PENDING);
        assertThat(dto.executeAt()).isEqualTo(scheduled.getExecuteAt());
    }

    @Test
    void createWithdrawalDTOsFromWithdrawalSchedules_shouldConvertList() {
        // Given
        WithdrawalScheduled w1 = new WithdrawalScheduled(100.0, Instant.now(), 1L, 10L, WithdrawalStatus.PENDING);
        WithdrawalScheduled w2 = new WithdrawalScheduled(200.0, Instant.now(), 2L, 20L, WithdrawalStatus.PENDING);
        List<WithdrawalScheduled> withdrawals = List.of(w1, w2);

        // When
        List<WithdrawalDTO> dtos = WithdrawalFactory.createWithdrawalDTOsFromWithdrawalSchedules(withdrawals);

        // Then
        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).amount()).isEqualTo(100.0);
        assertThat(dtos.get(1).amount()).isEqualTo(200.0);
    }

    @Test
    void createWithdrawalDTOsFromWithdrawals_shouldConvertLegacyWithdrawals() {
        // Given
        Withdrawal w1 = new Withdrawal(1L, 1L, 100.0, Instant.now(), 1L, 10L, WithdrawalStatus.SUCCESS);
        Withdrawal w2 = new Withdrawal(2L, 2L, 200.0, Instant.now(), 2L, 20L, WithdrawalStatus.FAILED);
        List<Withdrawal> withdrawals = List.of(w1, w2);

        // When
        List<WithdrawalDTO> result = WithdrawalFactory.createWithdrawalDTOsFromWithdrawals(withdrawals);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).transactionId()).isEqualTo(1L);
        assertThat(result.get(0).status()).isEqualTo(WithdrawalStatus.SUCCESS);
        assertThat(result.get(1).transactionId()).isEqualTo(2L);
        assertThat(result.get(1).status()).isEqualTo(WithdrawalStatus.FAILED);
    }
}
