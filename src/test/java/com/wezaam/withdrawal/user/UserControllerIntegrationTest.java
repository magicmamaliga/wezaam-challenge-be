package com.wezaam.withdrawal.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAll_shouldReturnUsers_andStatus200() throws Exception {

        // When & Then
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].firstName", notNullValue()));
    }

    @Test
    void findById_shouldReturnUser_andStatus200_whenUserExists() throws Exception {
        // Given
        Long existingUserId = 1L;

        // When & Then
        mockMvc.perform(get("/users/{id}", existingUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", notNullValue()));
    }

    @Test
    void findById_shouldReturn404_whenUserDoesNotExist() throws Exception {
        // Given
        Long nonExistingUserId = -1L;

        // When & Then
        mockMvc.perform(get("/users/{id}", nonExistingUserId))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturn400_whenIdIsInvalid() throws Exception {
        // When & Then
        mockMvc.perform(get("/users/invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Parameter 'id' must be of type 'Long'"));
    }
}
