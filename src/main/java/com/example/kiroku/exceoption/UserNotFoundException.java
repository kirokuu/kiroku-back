package com.example.kiroku.exceoption;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException() {
        super("찾을 수 없는 회원입니다.");
    }
}
