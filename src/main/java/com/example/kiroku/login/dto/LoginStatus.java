package com.example.kiroku.login.dto;

import lombok.Getter;

@Getter
public enum LoginStatus {
    SUCCESS("로그인에 성공하였습니다",200),
    FAIL("로그인에 실패하였습니다",400);


    private String message;
    private int code;

    LoginStatus(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
