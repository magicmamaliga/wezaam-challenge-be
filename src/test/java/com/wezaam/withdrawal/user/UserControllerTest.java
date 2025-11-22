package com.wezaam.withdrawal.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void findAll_shouldReturnListOfUsers_andStatus200() throws Exception {
        // Given
        List<UserDTO> users = List.of(
                new UserDTO(1L, "Mate", .1),
                new UserDTO(2L, "John", .2)
        );
        when(userService.findAll()).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Mate")))
                .andExpect(jsonPath("$[0].maxWithdrawalAmount", is(.1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("John")))
                .andExpect(jsonPath("$[1].maxWithdrawalAmount", is(.2)));
    }

    @Test
    void findById_shouldReturnUser_andStatus200() throws Exception {
        // Given
        Long id = 1L;
        UserDTO dto = new UserDTO(id, "Mate", .1);
        when(userService.findById(id)).thenReturn(dto);

        // When & Then
        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Mate")))
                .andExpect(jsonPath("$.maxWithdrawalAmount", is(.1)));

    }
}
