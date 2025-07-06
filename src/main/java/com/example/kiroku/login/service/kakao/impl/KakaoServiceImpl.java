package com.example.kiroku.login.service.kakao.impl;

import com.example.kiroku.login.dto.KakaoDto;
import com.example.kiroku.login.service.kakao.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientSsl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService{

    @Value( "${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String KAKAO_URL;
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String KAKAO_TOKEN_URL;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String KAKAO_USER_INFO_URL;
    @Value( "${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value( "${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    @Override
    public String getUrl() {
        return KAKAO_URL +
                "?response_type=code" +
                "&client_id=" + KAKAO_CLIENT_ID +
                "&redirect_uri=" + KAKAO_REDIRECT_URI;
    }

    @Override
    public String getAccessToken(String code) {
        KakaoDto.KakaoTokenResponse response = WebClient.create(KAKAO_TOKEN_URL).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .queryParam("client_id", KAKAO_CLIENT_ID)
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoDto.KakaoTokenResponse.class)
                .block();

        log.info(" [Kakao Service] Access Token ------> {}", response.getAccessToken());
        log.info(" [Kakao Service] Refresh Token ------> {}", response.getRefreshToken());

        log.info(" [Kakao Service] Id Token ------> {}", response.getIdToken());
        log.info(" [Kakao Service] Scope ------> {}", response.getScope());

        return response.getAccessToken();
    }

    @Override
    public KakaoDto.KakaoUserInfoResponse getUserInfo(String accessToken) {
        String response = WebClient.create(KAKAO_USER_INFO_URL).post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid token")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(String.class)
                .block();

        //json 응답을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoDto.KakaoUserInfoResponse userInfo = null;

        try {
            userInfo = objectMapper.readValue(response, KakaoDto.KakaoUserInfoResponse.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
        return userInfo;
    }
}
