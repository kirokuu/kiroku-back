package com.example.kiroku.login.service;

import com.example.kiroku.login.domain.User;
import com.example.kiroku.login.dto.LoginDto;
import com.example.kiroku.login.repository.UserRepository;
import com.example.kiroku.security.CustomUser;
import com.example.kiroku.security.util.JwtUtils;
import com.example.kiroku.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginDto.LoginResponse loginUser(String logindId, String password) {
        CustomUser customUser =(CustomUser) userDetailsService.loadUserByUsername(logindId);
        User user = passwordEncoder.matches(password, customUser.getPassword()) ? customUser.getUser() : User.emptyUser();
        LoginDto.LoginResponse response = LoginDto.getResponse(user);
        if(!user.isEmpty()) {
            String jwt = jwtUtils.generateTokenFromUsername(CustomUser.create(user));
            response.setToken(jwt);
        }
        return response;
    }
}
