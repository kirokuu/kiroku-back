package com.example.kiroku.user.service.impl;

import com.example.kiroku.login.domain.User;
import com.example.kiroku.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.kiroku.login.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findUser(String username, String password) {
        return  userRepository.findByUserIdAndPassword(username, password).orElse(User.emptyUser());
    }
}
