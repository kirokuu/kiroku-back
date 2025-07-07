package com.example.kiroku.login.service;

import com.example.kiroku.login.dto.KakaoDto;
import com.example.kiroku.login.dto.LoginDto;

public interface LoginService {
    LoginDto.LoginResponse loginUser(String username, String password);
    String getSocialLoginUrl();
    KakaoDto.KakaoUserInfoResponse getAccess(String code);
}
