package com.example.kiroku.login.dto;

import com.example.kiroku.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class LoginDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    public static class LoginRequest {
        @NotBlank(message="아이디는 필수입니다.")
        private String username;
        @NotBlank(message="비밀번호는 필수입니다.")
        private String password;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    public static class LoginResponse {
        private LoginStatus status;
        private String token;
        private String message;

        public LoginResponse succes(){
            this.status = LoginStatus.SUCCESS;
            this.message = "로그인 성공";
            return this;
        }

        public LoginResponse fail(){
            this.status = LoginStatus.FAIL;
            this.message = "로그인 실패";
            return this;
        }
    }

    public static LoginResponse getResponse(User user){
        LoginResponse loginResponse = new LoginResponse();
        if(user.isEmpty()) return loginResponse.fail();
        else return loginResponse.succes();
    }
}
