package com.example.kiroku.security.jwt;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor()
public class JwtTokens {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String refreshToken;
    private String userId;

    public JwtTokens(String token, String refreshToken, String userId){
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.token = token;
    }

    public static JwtTokens createRefreshToken(String token, String refreshToken, String userId){
       return new JwtTokens(token, refreshToken, userId);
    }

    public void updateToken(String token, String refreshToken){
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
