package com.example.kiroku.user.controller;

import com.example.kiroku.user.JoinStatus;
import com.example.kiroku.user.dto.JoinDto;
import com.example.kiroku.user.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/join")
@RequiredArgsConstructor
public class JoinUserController {

    private final JoinService joinService;

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
        if(result) return ResponseEntity.status(HttpStatus.IM_USED).build();
        else return ResponseEntity.ok().build();
    }

    @PostMapping("/out")
    @ResponseBody
    public ResponseEntity OutUserController() {
        return ResponseEntity.ok().build();

    }
}
