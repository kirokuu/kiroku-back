package com.example.kiroku.user.service;
import com.example.kiroku.login.service.Oauth2UserInfo;
import com.example.kiroku.user.domain.User;
import com.example.kiroku.user.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    User findUser(HttpServletRequest request);
    User findUser(String username, String password);
    User findUser(String userId);
    User findUserToJoin(String userName, String phoneNumber);
    User findUserByNickname(String nickname);
    User getSocialUser(Oauth2UserInfo oauth2UserInfo);
    UserDto.UserInfoResponse getUserInfo(String userId);
    User saveUser(User user);
    void deleteUser(User user);
}
