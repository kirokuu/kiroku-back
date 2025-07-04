package com.example.kiroku.com.security;

import com.example.kiroku.login.domain.User;
import com.example.kiroku.login.domain.type.UserType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Builder
@Getter @Setter
public class CustomUser implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserType role = user.getRole();
        return Collections.singletonList(()->role.getRole());
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }
}
