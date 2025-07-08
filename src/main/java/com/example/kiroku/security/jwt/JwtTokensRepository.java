package com.example.kiroku.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtTokensRepository extends JpaRepository<JwtTokens, Long> {
    Optional<JwtTokens> findByTokenAndUserId(String token, String userId);
}
