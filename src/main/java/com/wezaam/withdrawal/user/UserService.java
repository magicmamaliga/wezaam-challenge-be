package com.wezaam.withdrawal.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


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
     * @return a list of all {@link User} entities.
     */
    public List<User> findAll() {
        log.info("UserService findAll");
        return userRepository.findAll();
    }

    /**
     * Retrieves a {@link User} by their unique identifier.
     *
     * @param userId the ID of the user to retrieve. Must not be {@code null}.
     * @return the {@link User} entity if found.
     * @throws UserException           if the provided {@code userId} is {@code null}.
     * @throws UserNotFoundException   if no user with the given ID exists.
     */
    public User findById(Long userId) {
        log.info("UserService findById: {}", userId);
        if (userId == null) {
            throw new UserException("UserId is null");
        }
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

}
