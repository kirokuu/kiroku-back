package com.example.kiroku.login.service;

import com.example.kiroku.user.domain.User;
import com.example.kiroku.user.domain.type.UserType;
import jakarta.security.auth.message.AuthException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
public record Oauth2UserInfo(String name,
                             String email) {
    public static Oauth2UserInfo of(String registrationId, Map<String, Object> attributes) throws AuthException {
        return switch (registrationId){
            case "google" -> toGoogle(attributes);
            case "kakao" -> toKakao(attributes);
            default -> throw new AuthException("not supported registration id");
        };

    }

    public static Oauth2UserInfo toGoogle(Map<String, Object> attributes) {
        return Oauth2UserInfo.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .build();
    }

    public static Oauth2UserInfo toKakao(Map<String, Object> attributes) {
        String id = ((Long) attributes.get("id")).toString();
        Map<String, String> properties = (Map<String, String>) attributes.get("properties");

        return Oauth2UserInfo.builder()
                .name(properties.get("nickname"))
                .email(id)
                .build();
    }

    public User toUser(){
        return User.createUser(this.name,
                this.email,
                this.name,
                "",
                "",
                UserType.ROLE_USER
                );
    }
}
