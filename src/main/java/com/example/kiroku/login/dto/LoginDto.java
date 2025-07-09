package com.example.kiroku.login.dto;

import com.example.kiroku.user.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class LoginDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    public static class LoginRequest {
        @NotBlank(message="아이디는 필수입니다.")
        @JsonProperty("userId")
        private String username;
        @NotBlank(message="비밀번호는 필수입니다.")
        private String password;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    public static class LoginResponse {
        private LoginStatus status;
        private String accessToken;
        private String refreshToken;
        private String message;
        private String userId;

        public LoginResponse success(User user){
            this.status = LoginStatus.SUCCESS;
            this.message = "로그인에 성공하였습니다";
            this.userId = user.getUserId();

            return this;
        }

        public LoginResponse fail(){
            this.status = LoginStatus.FAIL;
            this.message = "로그인에 실패하였습니다";
            return this;
        }

        public void setTokens(String accessToken, String refreshToken){
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    public static LoginResponse getResponse(User user){
        LoginResponse loginResponse = new LoginResponse();
        if(user.isEmpty()) return loginResponse.fail();
        else return loginResponse.success(user);
    }
}
