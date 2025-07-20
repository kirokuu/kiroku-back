package com.example.kiroku.user.domain;

import com.example.kiroku.user.domain.type.UserType;
import com.example.kiroku.user.dto.JoinDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kirocu_id")
    private Long id;
    private String userId;
    private String username;
    private String nickname;
    private String password;
    @ColumnDefault(value = "01011112222")
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
        this.nickname = nickname;
        this.role = role;
    }

    private User(String nickname, String userId, String username, String password, UserType role){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    //User객체 최초생성(가입)
    public static User createUser(String nickname, String userId, String username, String password, String phoneNumber, UserType role){
        return new User(nickname,userId,username, password, phoneNumber, role);
    }
    public static User createUser(String nickname, String userId, String username, String password, UserType role){
        return new User(nickname,userId,username, password, role);
    }
    public static User createSocialUser(String nickname, String userId, UserType role){
        return new User(nickname,userId,nickname, "","", role);
    }

    public static User emptyUser(){
        User user = new User();
        user.isEmpty = true;
        return user;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
