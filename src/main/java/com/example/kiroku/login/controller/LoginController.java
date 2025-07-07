package com.example.kiroku.login.controller;

import com.example.kiroku.login.dto.KakaoDto;
import com.example.kiroku.login.dto.LoginDto;
import com.example.kiroku.login.dto.LoginStatus;
import com.example.kiroku.login.service.LoginService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/basic")
    @ResponseBody
    public ResponseEntity<LoginDto.LoginResponse> login(@RequestBody @Valid LoginDto.LoginRequest loginRequest) {
        LoginDto.LoginResponse loginResponse = loginService.loginUser(loginRequest.getUsername(), loginRequest.getPassword().toString());
        if(loginResponse.getStatus() == LoginStatus.FAIL) return ResponseEntity.badRequest().body(loginResponse);
        else return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/kakao")
    @ResponseBody
    public ResponseEntity<String> getKakaoUrl() {
        return ResponseEntity.ok(loginService.getSocialLoginUrl());
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<KakaoDto.KakaoUserInfoResponse> getKakaoUserInfo(@RequestParam("code") String code){
        return ResponseEntity.ok(loginService.getAccess(code));
    }


}
