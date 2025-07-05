package com.example.kiroku.user.service;
import com.example.kiroku.login.domain.User;

public interface UserService {
    User findUser(String username, String password);
}
