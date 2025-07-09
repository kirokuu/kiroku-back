package com.example.kiroku.security;

import com.example.kiroku.user.domain.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


@Getter @Setter
public class CustomUser implements UserDetails, OAuth2User {

    private final User user;
    private Map<String, Object> attributes;

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole().name();
            }
        });

        return collection;
    }

    public CustomUser(User user, Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }

    public CustomUser(User user){
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getName() {
        return user.getUserId();
    }

    public static CustomUser create(User user){
        return new CustomUser(user) ;
    }
}
