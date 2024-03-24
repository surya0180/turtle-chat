package com.turtlechat.socket.services;

import io.jsonwebtoken.Claims;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final TokenService tokenService;
    private final Logger logger = LogManager.getLogger(AuthenticationService.class);

    public AuthenticationService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public JSONObject validateUserToken(String token) {
        Claims claims = tokenService.validateJWTToken(token);
        JSONObject userData = new JSONObject();
        if(claims != null) {
            userData.put("userId", claims.get("userId"))
                    .put("userName", claims.get("userName"))
                    .put("userEmail", claims.get("userEmail"))
                    .put("isAuthenticated", true);
        } else {
            userData.put("isAuthenticated", false);
        }
        return userData;
    }
}
