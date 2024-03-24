package com.turtlechat.socket.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class TokenService {
    @Value("${turtle-chat.jwt-secret}")
    private String jwtSecret;

    private final Logger logger = LogManager.getLogger(TokenService.class);

    public Claims validateJWTToken(String token) {
        try {
            return this.extractAllClaims(token);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey().getEncoded())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }
}
