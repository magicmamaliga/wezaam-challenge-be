package com.wezaam.withdrawal.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

import static com.wezaam.withdrawal.user.UserToDTOFactory.createUserDTOFromUser;
import static com.wezaam.withdrawal.user.UserToDTOFactory.createUserDTOListFromUserList;


/**
 * Service class responsible for handling business logic related to {@link User} entities.
 * <p>
 * This service provides methods for retrieving user information from the persistence layer.
 * It logs all operations and throws custom exceptions where appropriate.
 * </p>
 */
@Slf4j
@Service
public class UserService {

    @Resource
    private UserRepository userRepository;

    /**
     * Retrieves all users from the database.
     *
     * @return a list of all {@link UserDTO} entities.
     */
    public List<UserDTO> findAll() {
        log.info("UserService findAll");
        return createUserDTOListFromUserList(userRepository.findAll());
    }

    /**
     * Retrieves a {@link UserDTO} by their unique identifier.
     *
     * @param userId the ID of the user to retrieve. Must not be {@code null}.
     * @return the {@link UserDTO} entity if found.
     * @throws UserException           if the provided {@code userId} is {@code null}.
     * @throws UserNotFoundException   if no user with the given ID exists.
     */
    public UserDTO findById(Long userId) {
        log.info("UserService findById: {}", userId);
        if (userId == null) {
            throw new UserException("UserId is null");
        }
        return createUserDTOFromUser(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found")));
    }

}
