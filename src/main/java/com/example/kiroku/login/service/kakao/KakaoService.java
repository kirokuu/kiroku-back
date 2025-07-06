package com.example.kiroku.login.service.kakao;

import com.example.kiroku.login.dto.KakaoDto;

public interface KakaoService {
    String getAccessToken(String code);
    KakaoDto.KakaoUserInfoResponse getUserInfo(String accessToken);
    String getUrl();
}
