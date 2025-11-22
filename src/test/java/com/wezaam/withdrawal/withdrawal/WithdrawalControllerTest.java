package com.wezaam.withdrawal.withdrawal;

import com.wezaam.withdrawal.withdrawal.dto.WithdrawalDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WithdrawalController.class)
class WithdrawalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WithdrawalService withdrawalService;


    @Test
    void scheduleWithdrawalRequest_shouldReturnCreatedWithdrawal_andStatus201() throws Exception {
        // Given
        WithdrawalDTO responseDTO = new WithdrawalDTO(
                1L, 123L, 100.0, Instant.now(), 1L, 10L, WithdrawalStatus.PENDING, Instant.now()
        );

        when(withdrawalService.schedule(any())).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 100.0,
                                  "executeAt": "ASAP",
                                  "userId": 1,
                                  "paymentMethodId": 10
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.transactionId", is(123)))
                .andExpect(jsonPath("$.amount", is(100.0)))
                .andExpect(jsonPath("$.status", is("PENDING")));

        verify(withdrawalService, times(1)).schedule(any());
    }

    @Test
    void scheduleWithdrawalRequest_shouldReturn400_whenInvalidRequest() throws Exception {
        // Given → missing required fields

        // When & Then
        mockMvc.perform(post("/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": null,
                                  "executeAt": "",
                                  "userId": null,
                                  "paymentMethodId": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(withdrawalService, never()).schedule(any());
    }


    @Test
    void findAll_shouldReturnListOfWithdrawals_andStatus200() throws Exception {
        // Given
        List<WithdrawalDTO> withdrawals = List.of(
                new WithdrawalDTO(1L, 123L, 100.0, Instant.now(), 1L, 10L, WithdrawalStatus.SUCCESS, null),
                new WithdrawalDTO(2L, 456L, 200.0, Instant.now(), 2L, 20L, WithdrawalStatus.FAILED, null)
        );

        when(withdrawalService.findAll()).thenReturn(withdrawals);

        // When & Then
        mockMvc.perform(get("/withdrawals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].transactionId", is(123)))
                .andExpect(jsonPath("$[0].status", is("SUCCESS")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].transactionId", is(456)))
                .andExpect(jsonPath("$[1].status", is("FAILED")));

        verify(withdrawalService, times(1)).findAll();
    }
}
