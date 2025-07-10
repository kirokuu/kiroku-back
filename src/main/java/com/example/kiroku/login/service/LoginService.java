package com.example.kiroku.login.service;

import com.example.kiroku.login.dto.KakaoDto;
import com.example.kiroku.login.dto.LoginDto;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {
    LoginDto.LoginResponse loginUser(String username, String password,  HttpServletResponse response);
    String getSocialLoginUrl();
    KakaoDto.KakaoUserInfoResponse getAccess(String code);
}
