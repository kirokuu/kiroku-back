package com.example.kiroku.login.controller;

import com.example.kiroku.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public ResponseEntity<String> login(Authentication authentication) {
        loginService.loginUser(authentication.getName(), authentication.getCredentials().toString());
        return ResponseEntity.ok("Login Success");
    }
}
