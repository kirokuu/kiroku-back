package com.example.kiroku.security;

import com.example.kiroku.login.domain.User;
import com.example.kiroku.login.domain.type.UserType;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


@Getter @Setter
public class CustomUser implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserType role = user.getRole();
        return Collections.singletonList(()->role.getRole());
    }

    public CustomUser(User user){
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    public static CustomUser create(User user){
        return new CustomUser(user) ;
    }
}
