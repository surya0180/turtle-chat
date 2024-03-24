package com.turtlechat.server.utils;

import com.turtlechat.server.models.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class TokenValidator {
    @Value("${turtle-chat.jwt-secret}")
    private String jwtSecret;

    private final Logger logger = LogManager.getLogger(TokenValidator.class);

    public String generateJWTToken(User user) {
        return this.createJWTToken(user);
    }

    public Claims validateJWTToken(String token)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException {
        return this.extractAllClaims(token);
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String jwtToken)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private String createJWTToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 24*60*60*1000);

        return Jwts.builder()
                .setSubject(user.getUserId().toString())
                .claim("userId", user.getUserId())
                .claim("userName", user.getUserName())
                .claim("userEmail", user.getUserEmail())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
