package com.example.kiroku.user.dto;

import com.example.kiroku.user.domain.User;
import com.example.kiroku.user.domain.type.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinDto {
    @NotNull(message = "아디디를 입력바랍니다.")
    private String userId;
    @NotNull(message = "이름을 입력바랍니다.")
    private String username;
    @NotNull(message = "비밀번호를 확인바랍니다.")
    private String password;
    private String phoneNumber;

    public User ofUser(){
        if (this.phoneNumber == null || this.phoneNumber.isEmpty()) return User.createUser(this.username,
                this.userId,
                this.username,
                this.password,
                UserType.ROLE_USER);

        return User.createUser(this.username, this.userId, this.username, this.password, this.phoneNumber, UserType.ROLE_USER);
    }
}
