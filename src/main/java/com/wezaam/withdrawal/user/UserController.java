package com.wezaam.withdrawal.user;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api
@Slf4j
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/find-all-users")
    public List<User> findAll() {
        log.info("UserService findAll");
        return userService.findAll();
    }

    @GetMapping("/find-user-by-id/{id}")
    public User findById(@PathVariable Long id) {
        log.info("UserService findById: {}", id);
        return userService.findById(id);
    }
}
