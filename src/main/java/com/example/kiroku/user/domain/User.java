package com.example.kiroku.user.domain;

import com.example.kiroku.user.domain.type.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter
@NoArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String username;
    private String nickname;
    private String password;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserType role;
    @Transient
    private boolean isEmpty = false;

    public User(String nickname, String userId,String username, String password, String phoneNumber, UserType role){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nickname = username;
        this.role = role;
    }

    //User객체 최초생성(가입)
    public static User createUser(String nickname, String userId, String username, String password, String phoneNumber, UserType role){
        return new User(nickname,userId,username, password, phoneNumber, role);
    }

    public static User emptyUser(){
        User user = new User();
        user.isEmpty = true;
        return user;
    }
}
