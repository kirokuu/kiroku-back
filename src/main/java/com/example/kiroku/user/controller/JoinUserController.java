package com.example.kiroku.user.controller;

import com.example.kiroku.login.service.LoginService;
import com.example.kiroku.security.util.JwtProvider;
import com.example.kiroku.user.JoinStatus;
import com.example.kiroku.user.domain.User;
import com.example.kiroku.user.dto.JoinDto;
import com.example.kiroku.user.service.JoinService;
import com.example.kiroku.user.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/join")
@RequiredArgsConstructor
@Tag(name = "사용자 등록", description = "회원가입, ID/닉네임 검증, 계정 탈퇴를 위한 API")
public class JoinUserController {

    private final JoinService joinService;
    private final UserService userService;
    private final LoginService loginService;

    @PostMapping("/in")
    @Operation(summary = "신규 회원 등록", description = "신규 회원 계정을 생성합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 입력 또는 회원가입 실패")
    })
    public ResponseEntity<JoinStatus> join(@Valid @RequestBody JoinDto joinDto) {
        JoinStatus status = joinService.joinUser(joinDto);
        if (status.isSuccess()) return ResponseEntity.ok(status);
        else return ResponseEntity.badRequest().body(status.isFail());
    }

    @GetMapping("/checkid")
    @Operation(summary = "회원 ID 사용 가능 여부 확인", description = "제공된 회원 ID가 이미 사용 중인지 확인합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 ID 사용 가능"),
        @ApiResponse(responseCode = "226", description = "회원 ID가 이미 사용 중 (IM_USED)")
    })
    public ResponseEntity<Boolean> checkDuplicateId(@RequestParam("userId") String userId) {
        boolean result = joinService.checkDuplicateId(userId);
        if(!result) return ResponseEntity.status(HttpStatus.IM_USED).build();
        else return ResponseEntity.ok().build();
    }

    @GetMapping("/checkNickname")
    @ResponseBody
    @Operation(summary = "닉네임 사용 가능 여부 확인", description = "제공된 닉네임이 이미 사용 중인지 확인합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "닉네임 사용 가능"),
        @ApiResponse(responseCode = "409", description = "닉네임이 이미 사용 중 (CONFLICT)")
    })
    public ResponseEntity checkDuplicateNickname(@RequestParam("nickname") String nickname) {
        boolean result = joinService.checkDuplicateNickname(nickname);
        if(!result) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        else return ResponseEntity.ok().build();
    }

    @PostMapping("/withdrawal")
    @ResponseBody
    @Operation(summary = "사용자 계정 탈퇴", description = "회원 계정을 비활성화하고 로그아웃합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "계정이 성공적으로 탈퇴됨"),
        @ApiResponse(responseCode = "401", description = "인증되지 않음, 사용자가 인증되지 않음"),
        @ApiResponse(responseCode = "404", description = "회원 계정을 찾을 수 없음")
    })
    public ResponseEntity withdrawalController(HttpServletRequest request, HttpServletResponse response) {
        User user = userService.findUser(request);
        if(user.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        loginService.logout(request, response);
        boolean result = joinService.withdrawal(user.getUserId());

        if(!result) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok().build();
    }
}
