package com.example.demo.JWT;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // 토큰 검증 (3개의 메세드가 토큰 검증의 과정임)

    // 페이로드에서 username 가져와서 검증
    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    // 페이로드에서 role 가져와서 검증
    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    // 현재 시간값을 넣어서, 익스파이어한지 안한지 페이로드에서 가져와 검증
    public boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }


    // 토큰 생성
    public String createJwt(String username, String role, Long expiredMs) {
        System.out.println("createJwt");
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis())) // 언제 발행됐는지(현재 기준 시간을 발행시간으로 지정
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 언제까지 살아있을지. (언제 소멸될건지)
                .signWith(secretKey) // 시크릿키를 통해 암호화를 진행
                .compact();
    }
}
