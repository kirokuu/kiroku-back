package com.example.kiroku.user.controller;

import com.example.kiroku.login.service.LoginService;
import com.example.kiroku.security.util.JwtProvider;
import com.example.kiroku.user.JoinStatus;
import com.example.kiroku.user.domain.User;
import com.example.kiroku.user.dto.JoinDto;
import com.example.kiroku.user.service.JoinService;
import com.example.kiroku.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/join")
@RequiredArgsConstructor
public class JoinUserController {

    private final JoinService joinService;
    private final UserService userService;
    private final LoginService loginService;

    @PostMapping("/in")
    @ResponseBody
    public ResponseEntity<JoinStatus> JoinUserController(JoinDto.JoinRequest joinRequest) {
        JoinStatus status = joinService.joinUser(joinRequest);
        if (status.isSuccess()) return ResponseEntity.ok(status);
        else return ResponseEntity.badRequest().body(status.isFail());
    }

    @GetMapping("/checkId")
    @ResponseBody
    public ResponseEntity<Boolean> checkDuplicateId(@RequestParam("userId") String userId) {
        boolean result = joinService.checkDuplicateId(userId);
        if(result) return ResponseEntity.status(HttpStatus.IM_USED).build();
        else return ResponseEntity.ok().build();
    }

    @GetMapping("/checkNickname")
    @ResponseBody
    public ResponseEntity checkDuplicateNickname(@RequestParam("nickname") String nickname) {
        boolean result = joinService.checkDuplicateNickname(nickname);
        if(result) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        else return ResponseEntity.ok().build();
    }

    @PostMapping("/withdrawal")
    @ResponseBody
    public ResponseEntity withdrawalController(HttpServletRequest request, HttpServletResponse response) {
        User user = userService.findUser(request);
        if(user.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        loginService.logout(request, response);
        boolean result = joinService.withdrawal(user.getUserId());

        if(!result) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok().build();
    }
}
