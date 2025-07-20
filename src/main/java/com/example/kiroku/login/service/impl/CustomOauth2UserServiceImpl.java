package com.example.kiroku.login.service.impl;

import com.example.kiroku.login.service.LoginService;
import com.example.kiroku.login.service.Oauth2UserInfo;
import com.example.kiroku.security.CustomUser;
import com.example.kiroku.user.domain.User;
import com.example.kiroku.user.service.UserService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOauth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        log.info("userNameAttributeName : {}", userNameAttributeName);
        //TODO null??꼴보기가 싫다 구조를 고치자
        Oauth2UserInfo oauth2UserInfo = null;
        try {
             oauth2UserInfo = Oauth2UserInfo.of(registrationId, attributes);
        } catch (AuthException e) {
            throw new RuntimeException(e.getMessage() + " : " + registrationId, e);
        }

        User user = userService.getSocialUser(oauth2UserInfo);
        CustomUser customUser = new CustomUser(user, attributes);

        return customUser;
    }
}
