package com.example.kiroku.security.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class JwtTokenDto {
    
    @NoArgsConstructor
    @Getter @Setter
    public static class JwtTokenResponse {
        private String accessToken;
        private String refreshToken;
        private String userId;
        private String message;

        public JwtTokenResponse(String accessToken, String refreshToken, String userId, String message){
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.userId = userId;
            this.message = message;
        }
    }
}
