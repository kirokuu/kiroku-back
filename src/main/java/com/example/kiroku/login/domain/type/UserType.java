package com.example.kiroku.login.domain.type;

import lombok.Getter;

@Getter
public enum UserType {

    ROLE_ADMIN ("admin","관리자"), ROLE_USER("user","일반사용자");

    private String role;
    private String name;

    UserType(String role, String name){
        this.role = role;
        this.name = name;
    }
}
