package com.example.kiroku.security.util;

import com.example.kiroku.security.jwt.JwtTokens;
import com.example.kiroku.security.jwt.JwtTokensRepository;
import com.example.kiroku.security.util.exception.TokenException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_GRANT;
import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_TOKEN;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    private final JwtTokensRepository jwtTokensRepository;
    //seretkey값
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    private static final long TOKEN_EXPIRATION_MS =  1000 * 60 * 30L;;

    private static final long REFRESH_TOKEN_EXPIRATION_MS = 1000 * 60 * 60L * 24 * 7;;

    @Value("${spring.app.role}")
    private String KEY_ROLE;


    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove Bearer prefix
        }
        return null;
    }
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);

        // 2. security의 User 객체 생성
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, TOKEN_EXPIRATION_MS);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, REFRESH_TOKEN_EXPIRATION_MS);
    }

    public String generateTokenFromUsername(UserDetails userDetails) {
        return generateToken(userDetails, TOKEN_EXPIRATION_MS);
    }

    public String generateRefreshTokenFromUsername(UserDetails userDetails) {
        return generateToken(userDetails, REFRESH_TOKEN_EXPIRATION_MS);
    }

    private String generateToken(Authentication authentication, long expireTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        return Jwts.builder()
                .subject(authentication.getName())
                .claim(KEY_ROLE, authorities)
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(key())
                .compact();
    }

    private String generateToken(UserDetails userDetails, long expireTime) {
        String username = userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + expireTime))
                .signWith(key())
                .compact();
    }

    public void saveRefreshToken(String token, String refreshToken ,String userId) {
        jwtTokensRepository.save(JwtTokens.createRefreshToken(token, refreshToken, userId));
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority(
                claims.get(KEY_ROLE).toString()));
    }

    public String jwtTokenReissuer(String token){
        String userId = getUserNameFromJwtToken(token);
        JwtTokens refreshToken = jwtTokensRepository.findByTokenAndUserId(token, userId)
                .orElseThrow(() -> new RuntimeException("Refresh Token is not found"));

        Authentication authentication = getAuthentication(token);

        String newToken = generateToken(authentication, TOKEN_EXPIRATION_MS);
        String newRefreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRATION_MS);

        refreshToken.updateToken(newToken, newRefreshToken);

        return refreshToken.getRefreshToken();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    private Claims parseClaims(String token) throws TokenException {
        try {
            return Jwts.parser().verifyWith((SecretKey)key()).build()
                    .parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (MalformedJwtException e) {
            throw new TokenException(INVALID_TOKEN);
        } catch (SecurityException e) {
            throw new TokenException(INVALID_GRANT);
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            System.out.println("Validate");
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
