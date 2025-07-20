package com.example.kiroku.user.dto;

import com.example.kiroku.dto.ResponseResult;
import com.example.kiroku.user.domain.User;
import com.example.kiroku.user.domain.type.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class UserDto {

    @Getter @Setter
    @NoArgsConstructor
    public static class UserInfoNickName {
        private String nickname;
    }

    @Getter @Setter
    @NoArgsConstructor
    public static class UserInfoResponse extends ResponseResult {
        private String userId;
        private String username;
        private String nickname;
        private String password;
        private String phoneNumber;
        private String userType;

        public UserInfoResponse(String userId, String username, String nickname, String password, String phoneNumber, UserType userType){
            this.userId = userId;
            this.username = username;
            this.nickname = nickname;
            this.password = password;
            this.phoneNumber = phoneNumber;
            this.userType = userType.name();
        }

        public static UserInfoResponse of(User user){
            return new UserInfoResponse(user.getUserId(),
                    user.getUsername(),
                    user.getNickname(),
                    user.getPassword(),
                    user.getPhoneNumber(),
                    user.getRole());
        }

        public static UserInfoResponse empty(){
            return new UserInfoResponse();
        }

        @Override
        protected void setResult() {
            this.setResult(this);
            if(this.userId != null) basicSuccessSet();
            else basicFailSet();
        }
    }
}
