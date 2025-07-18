package com.example.kiroku.user.dto;

import com.example.kiroku.ResponseResult;
import com.example.kiroku.user.domain.User;
import com.example.kiroku.user.domain.type.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class JoinDto {

    @Getter @Setter
    @NoArgsConstructor
    public static class JoinRequest{
        private String userId;
        private String username;
        private String password;
        private String phoneNumber;

        public JoinRequest(String userId, String username, String password, String phoneNumber){
            this.userId = userId;
            this.username = username;
            this.password = password;
            this.phoneNumber = phoneNumber;
        }

        public User ofUser(){
            return new User(this.username, this.userId, this.username, this.password, this.phoneNumber, UserType.ROLE_USER);
        }
    }
}
