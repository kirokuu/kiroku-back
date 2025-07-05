package com.example.kiroku.login.service;

import com.example.kiroku.login.dto.LoginDto;

public interface LoginService {
    LoginDto.LoginResponse loginUser(String username, String password);
}
