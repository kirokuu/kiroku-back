package com.example.kiroku.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtTokensRepository extends JpaRepository<JwtTokens, Long> {
    Optional<JwtTokens> findByRefreshToken(String refreshToken);
}
