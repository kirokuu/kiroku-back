package com.example.kiroku.user.service.impl;

import com.example.kiroku.login.service.Oauth2UserInfo;
import com.example.kiroku.user.domain.User;
import com.example.kiroku.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.kiroku.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findUser(String username, String password) {
        return  userRepository.findByUserIdAndPassword(username, password).orElse(User.emptyUser());
    }

    @Override
    public User getSocialUser(Oauth2UserInfo oauth2UserInfo) {
        String name = oauth2UserInfo.name();
        String email = oauth2UserInfo.email();
        return userRepository.findByUserIdAndNickname(email, name).orElse(
                userRepository.save(oauth2UserInfo.toUser())
        );
    }
}
