package com.example.kiroku.user.service;
import com.example.kiroku.login.service.Oauth2UserInfo;
import com.example.kiroku.user.domain.User;

public interface UserService {
    User findUser(String username, String password);
    User findUser(String userId);
    User findUserToJoin(String userName, String phoneNumber);
    User findUserByNickname(String nickname);
    User getSocialUser(Oauth2UserInfo oauth2UserInfo);
    User saveUser(User user);
}
