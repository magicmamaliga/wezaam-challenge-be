package com.wezaam.withdrawal.withdrawal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WithdrawalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void scheduleWithdrawalRequest_shouldReturn201_andCreateWithdrawal() throws Exception {
        // When & Then
        mockMvc.perform(post("/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 1,
                                  "paymentMethodId": 1,
                                  "amount": 100.0,
                                  "executeAt": "ASAP"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.paymentMethodId", is(1)))
                .andExpect(jsonPath("$.amount", is(100.0)))
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.transactionId", nullValue()));
    }

    @Test
    void scheduleWithdrawalRequest_shouldReturn400_whenMissingRequiredField() throws Exception {
        // When & Then
        mockMvc.perform(post("/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 1,
                                  "paymentMethodId": 1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("must not be null")));
    }

    @Test
    void scheduleWithdrawalRequest_shouldReturn400_whenInvalidAmount() throws Exception {
        // When & Then
        mockMvc.perform(post("/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 1,
                                  "paymentMethodId": 1,
                                  "amount": -10.0,
                                  "executeAt": "ASAP"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("must be greater than 0")));
    }

    @Test
    void findAll_shouldReturnWithdrawalsList() throws Exception {
        // Given – create one withdrawal first
        mockMvc.perform(post("/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 1,
                                  "paymentMethodId": 1,
                                  "amount": 50.0,
                                  "executeAt": "ASAP"
                                }
                                """))
                .andExpect(status().isCreated());

        // When & Then → now call GET
        mockMvc.perform(get("/withdrawals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].amount", is(50.0)))
                .andExpect(jsonPath("$[0].status", is("PENDING")));
    }
}
