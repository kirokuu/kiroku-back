package com.example.kiroku.login.service;

import com.example.kiroku.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{
    private UserRepository userRepository;

    @Override
    public void loginUser(String username, String password) {
        //TODO returnType 변경
        userRepository.findByUsernameAndPassword(username, password);
    }
}
