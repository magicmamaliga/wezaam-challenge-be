package com.wezaam.withdrawal.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void findById_shouldReturnUser_whenUserExistsInDatabase() {
        // Given
        Long existingUserId = 1L;

        // When
        UserDTO result = userService.findById(existingUserId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(existingUserId);
        assertThat(result.firstName()).isNotBlank();
    }

    @Test
    void findById_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        // Given
        Long nonExistingUserId = -1L;

        // When / Then
        assertThatThrownBy(() -> userService.findById(nonExistingUserId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void findById_shouldThrowUserBadRequestException_whenUserIdIsNull() {
        // When / Then
        assertThatThrownBy(() -> userService.findById(null))
                .isInstanceOf(UserBadRequestException.class)
                .hasMessage("UserId is null");
    }

    @Test
    void findAll_shouldReturnNonEmptyList_whenDatabaseHasUsers() {
        // When
        var result = userService.findAll();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).id()).isNotNull();
    }
}
