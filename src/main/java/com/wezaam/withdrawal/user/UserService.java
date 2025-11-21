package com.wezaam.withdrawal.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Resource
    private UserRepository userRepository;

    public List<User> findAll() {
        log.info("UserService findAll");
        return userRepository.findAll();
    }

    public User findById(Long userId) {
        log.info("UserService findById: {}", userId);
        if (userId == null) {
            throw new UserException("UserId is null");
        }
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

}
