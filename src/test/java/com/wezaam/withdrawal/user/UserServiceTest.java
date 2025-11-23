package com.wezaam.withdrawal.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findAll_shouldReturnUserDTOList() {
        // Given
        User user1 = new User(1L, "Mate");
        User user2 = new User(2L, "John");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // When
        List<UserDTO> result = userService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).firstName()).isEqualTo("Mate");
        assertThat(result.get(1).id()).isEqualTo(2L);
        assertThat(result.get(1).firstName()).isEqualTo("John");
    }

    @Test
    void findById_shouldReturnUserDTO_whenExists() {
        // Given
        Long id = 1L;
        User user = new User(id, "Mate");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // When
        UserDTO result = userService.findById(id);

        // Then
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.firstName()).isEqualTo("Mate");
    }

    @Test
    void findById_shouldThrowBadRequest_whenUserIdIsNull() {
        assertThatThrownBy(() -> userService.findById(null))
                .isInstanceOf(UserBadRequestException.class)
                .hasMessage("UserId is null");
    }

    @Test
    void findById_shouldThrowNotFound_whenUserDoesNotExist() {
        //Given
        Long id = 99L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> userService.findById(id))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
    }
}
