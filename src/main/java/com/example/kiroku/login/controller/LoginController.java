package com.example.kiroku.login.controller;

import com.example.kiroku.dto.ResponseResult;
import com.example.kiroku.login.dto.LoginDto;
import com.example.kiroku.login.dto.LoginStatus;
import com.example.kiroku.login.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "사용자 로그인", description = "기본 로그인, 소셜 로그인, 로그아웃을 위한 API")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    @ResponseBody
    @Operation(summary = "기본 로그인", description = "사용자 이름과 비밀번호를 사용하여 로그인합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공 및 토큰 반환"),
        @ApiResponse(responseCode = "400", description = "인증오류 또는 로그인 실패")
    })
    public ResponseEntity<ResponseResult> login(@RequestBody @Valid LoginDto.LoginRequest loginRequest, HttpServletResponse response) {
        LoginDto.LoginResponse loginResponse = loginService.loginUser(loginRequest.getUsername(), loginRequest.getPassword().toString(), response);
        ResponseResult result = loginResponse.getResult();
        if(result.getCode() == LoginStatus.FAIL.getCode()) return ResponseEntity.badRequest().body(result);
        else return ResponseEntity.ok(result);
    }

    @GetMapping("/kakao")
    @ResponseBody
    public ResponseEntity<String> getKakaoUrl() {
        return ResponseEntity.ok(loginService.getSocialLoginUrl());
    }

    /*@GetMapping("/kakao/callback")
    public ResponseEntity<KakaoDto.KakaoUserInfoResponse> getKakaoUserInfo(@RequestParam("code") String code){
        return ResponseEntity.ok(loginService.getAccess(code));
    }*/

   /* @GetMapping("/oauth/success")
    @Operation(summary = "OAuth 인증 성공 처리", description = "OAuth 인증 성공 후 토큰을 처리합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OAuth 인증 성공 및 토큰 반환")
    })
    public ResponseEntity<String> test(@RequestParam("token") String token){
        return ResponseEntity.ok(token);
    }*/

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "사용자를 로그아웃하고 세션을 무효화합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        loginService.logout(request, response);
        return ResponseEntity.ok().build();
    }
}
