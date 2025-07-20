package com.example.kiroku.user.controller;

import com.example.kiroku.user.domain.User;
import com.example.kiroku.user.dto.UserDto;
import com.example.kiroku.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserDto.UserInfoResponse> getUserInfo(HttpServletRequest request){
        User user = userService.findUser(request);
        if(user.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        UserDto.UserInfoResponse userInfo = userService.getUserInfo(user.getUserId());
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/update/nickname")
    public ResponseEntity updateNickname(@RequestBody String nickname, HttpServletRequest request){
        User user = userService.findUser(request);
        userService.updateNickname(user, nickname);
        return ResponseEntity.ok().build();
    }
}
