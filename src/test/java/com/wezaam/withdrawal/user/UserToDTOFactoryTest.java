package com.wezaam.withdrawal.user;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class UserToDTOFactoryTest {

    @Test
    void createUserDTOFromUser_shouldReturnDTO_whenUserIsValid() {
        // Given
        User user = new User(1L, "Mate", 500.0);

        // When
        UserDTO result = UserToDTOFactory.createUserDTOFromUser(user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.firstName()).isEqualTo("Mate");
        assertThat(result.maxWithdrawalAmount()).isEqualTo(500.0);
    }

    @Test
    void createUserDTOFromUser_shouldReturnNull_whenUserIsNull() {
        // Given
        User user = null;

        // When
        UserDTO result = UserToDTOFactory.createUserDTOFromUser(user);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void createUserDTOListFromUserList_shouldConvertAllUsers() {
        // Given
        User user1 = new User(1L, "Mate", 500.0);
        User user2 = new User(2L, "John", 300.0);
        List<User> users = List.of(user1, user2);

        // When
        List<UserDTO> result = UserToDTOFactory.createUserDTOListFromUserList(users);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).firstName()).isEqualTo("Mate");
        assertThat(result.get(0).maxWithdrawalAmount()).isEqualTo(500.0);

        assertThat(result.get(1).id()).isEqualTo(2L);
        assertThat(result.get(1).firstName()).isEqualTo("John");
        assertThat(result.get(1).maxWithdrawalAmount()).isEqualTo(300.0);
    }
}
