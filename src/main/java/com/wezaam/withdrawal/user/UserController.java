package com.wezaam.withdrawal.user;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api
@Slf4j
@Validated
@RestController("/users")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping
    public List<User> findAll() {
        log.info("UserService findAll");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable @NotNull Long id) {
        log.info("UserService findById: {}", id);
        return userService.findById(id);
    }
}
