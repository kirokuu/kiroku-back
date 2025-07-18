package com.example.kiroku.user;

import lombok.Getter;

@Getter
public enum JoinStatus {

    DUPLICATE_ID("중복되는 아이디입니다."),
    DUPLICATE_NICKNAME("중복되는 닉네임입니다."),
    ALREADY_JOIN("이미 가입한 회원입니다."),
    SUCCESS("가입에 성공하였습니다");

    private String message;

    JoinStatus(String message) {
        this.message = message;
    }

    public boolean isSuccess(){
        return this == SUCCESS;
    }
    public JoinStatus isFail(){
        return this;
    }
}
