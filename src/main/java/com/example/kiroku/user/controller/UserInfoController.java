package com.example.kiroku.user.controller;

import com.example.kiroku.dto.ResponseResult;
import com.example.kiroku.user.domain.User;
import com.example.kiroku.user.dto.UserDto;
import com.example.kiroku.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "사용자 정보", description = "회원정보조회, 정보업데이트")
public class UserInfoController {

    private final UserService userService;

    @GetMapping("/info")
    @Operation(summary = "회원 정보 조회", description = "회원정보를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원정보 조회 성공"),
            @ApiResponse(responseCode = "401", description = "찾을 수 없는 회원")
    })
    public ResponseEntity<ResponseResult> getUserInfo(HttpServletRequest request){
        User user = userService.findUser(request);
        if(user.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                body(UserDto.UserInfoResponse.empty().
                        getCustomResult("찾을수 없는 회원입니다",HttpStatus.UNAUTHORIZED.value()));
        UserDto.UserInfoResponse userInfo = userService.getUserInfo(user.getUserId());
        return ResponseEntity.ok(userInfo.getResult());
    }

    @PostMapping("/update/nickname")
    @Operation(summary = "회원 닉네임 업데이트", description = "닉네임을 설정합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 설정 성공", content = @Content()),
            @ApiResponse(responseCode = "401", description = "찾을 수 없는 회원", content = @Content())
    })
    public ResponseEntity updateNickname(@RequestBody UserDto.UserInfoNickName userNickname, HttpServletRequest request){
        User user = userService.findUser(request);
        userService.updateNickname(user, userNickname.getNickname());
        return ResponseEntity.ok().build();
    }
}
