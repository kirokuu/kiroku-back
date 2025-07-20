package com.example.kiroku.user.dto;

import com.example.kiroku.dto.ResponseResult;
import com.example.kiroku.user.domain.User;
import com.example.kiroku.user.domain.type.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinDto {
    @NotNull(message = "아이디를 입력바랍니다.")
    @Schema(required = true)
    private String userId;
    @NotNull(message = "비밀번호를 확인바랍니다.")
    @Schema(required = true)
    private String password;
    private String phoneNumber;

    public User ofUser(){
        if (this.phoneNumber == null || this.phoneNumber.isEmpty()) return User.createUser(
                this.userId,
                this.password,
                UserType.ROLE_USER);

        return User.createUser( this.userId, this.password, this.phoneNumber, UserType.ROLE_USER);
    }

    @Getter @Setter
    public static class CheckIdRequest {
        @Schema(required = true)
        private String userId;
        public CheckIdRequest(String userId){
            this.userId = userId;
        }
    }

    @Getter @Setter
    public static class CheckNickname {
        @Schema(required = true)
        private String nickname;
        public CheckNickname(String nickname){
            this.nickname = nickname;
        }
    }
}
