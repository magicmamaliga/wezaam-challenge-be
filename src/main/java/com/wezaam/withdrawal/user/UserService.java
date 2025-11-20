package com.wezaam.withdrawal.user;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    UserRepository userRepository;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findById(Long userId){
        if(userId==null){
            throw new UserException("UserId is null");
        }
        return userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
    }

}
