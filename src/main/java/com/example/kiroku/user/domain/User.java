package com.example.kiroku.login.domain;

import com.example.kiroku.login.domain.type.UserType;
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
    private String password;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserType role;
    @Transient
    private boolean isEmpty = false;

    public User(String userId,String username, String password, String phoneNumber, UserType role){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    //User객체 최초생성(가입)
    public static User createUser(String userId, String username, String password, String phoneNumber, UserType role){
        return new User(userId,username, password, phoneNumber, role);
    }

    public static User emptyUser(){
        User user = new User();
        user.isEmpty = true;
        return user;
    }
}
