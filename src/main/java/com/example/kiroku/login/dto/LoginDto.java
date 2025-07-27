package com.example.kiroku.login.dto;

import com.example.kiroku.dto.ResponseResult;
import com.example.kiroku.user.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class LoginDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    public static class LoginRequest {
        @NotBlank(message="아이디는 필수입니다.")
        @Schema(required = true)
        @JsonProperty("userId")
        private String username;
        @NotBlank(message="비밀번호는 필수입니다.")
        @Schema(required = true)
        private String password;
    }


}
