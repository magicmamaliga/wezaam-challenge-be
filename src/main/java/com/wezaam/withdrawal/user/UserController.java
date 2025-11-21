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

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@Tag(name = "User API", description = "Operations related to users")
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "Get all users")
    @GetMapping
    public List<User> findAll() {
        log.info("UserService findAll");
        return userService.findAll();
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public User findById(@PathVariable @NotNull Long id) {
        log.info("UserService findById: {}", id);
        return userService.findById(id);
    }
}
