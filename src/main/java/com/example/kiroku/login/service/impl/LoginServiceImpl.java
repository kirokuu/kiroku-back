package com.example.kiroku.login.service.impl;

import com.example.kiroku.user.domain.User;
import com.example.kiroku.login.dto.KakaoDto;
import com.example.kiroku.login.dto.LoginDto;
import com.example.kiroku.login.service.LoginService;
import com.example.kiroku.login.service.kakao.KakaoService;
import com.example.kiroku.security.CustomUser;
import com.example.kiroku.security.util.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final KakaoService kakaoService;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginDto.LoginResponse loginUser(String logindId, String password, HttpServletResponse response) {
        CustomUser customUser =(CustomUser) userDetailsService.loadUserByUsername(logindId);
        User user = passwordEncoder.matches(password, customUser.getPassword()) ? customUser.getUser() : User.emptyUser();
        LoginDto.LoginResponse result = LoginDto.getResponse(user);
        if(!user.isEmpty()) {
            String jwtToken = jwtProvider.generateTokenFromUsername(CustomUser.create(user));
            String refreshToken = jwtProvider.generateRefreshTokenFromUsername(CustomUser.create(user));

            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(Duration.ofDays(7))
                    .sameSite("Strict")
                    .build();

            response.setHeader(AUTHORIZATION, "Bearer " + jwtToken);
            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            jwtProvider.saveRefreshToken(jwtToken, refreshToken, user.getUserId());
            result.setTokens(jwtToken, refreshToken);
        }
        return result;
    }

    @Override
    public String getSocialLoginUrl() {
        return kakaoService.getUrl();
    }

    @Override
    public KakaoDto.KakaoUserInfoResponse getAccess(String code) {
        String accessToken = kakaoService.getAccessToken(code);
        return kakaoService.getUserInfo(accessToken);
    }
}
