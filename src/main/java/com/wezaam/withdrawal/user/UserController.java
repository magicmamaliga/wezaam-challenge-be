package com.wezaam.withdrawal.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * REST controller that handles User API requests.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@Tag(name = "User API", description = "Operations related to users")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * Retrieves a list of all users.
     *
     * @return a {@link List} of {@link User} objects representing all users.
     */
    @Operation(summary = "Get all users")
    @GetMapping
    public List<User> findAll() {
        log.info("UserService findAll");
        return userService.findAll();
    }

    /**
     * Retrieves the details of a specific user by their unique identifier.
     *
     * @param id the ID of the user to be retrieved; must not be {@code null}.
     * @return the {@link User} object matching the provided ID.
     */
    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public User findById(@PathVariable @NotNull Long id) {
        log.info("UserService findById: {}", id);
        return userService.findById(id);
    }
}
