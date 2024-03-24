package com.turtlechat.socket.config;

import com.turtlechat.socket.services.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtTokenInterceptor implements HandshakeInterceptor {
    private final Logger logger = LogManager.getLogger(JwtTokenInterceptor.class);
    private final AuthenticationService authService;

    public JwtTokenInterceptor(AuthenticationService authService) {
        this.authService = authService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        try {
            String query = request.getURI().getQuery();
            String[] params = query.split("&");

            if(params.length != 2) {
                return false;
            }

            String roomId = params[0].split("=")[1];
            String token = params[1].split("=")[1];
            JSONObject userData = authService.validateUserToken(token);

            attributes.put("roomId", Long.parseLong(roomId));
            attributes.put("userData", userData);

            boolean isAuthenticated = userData.getBoolean("isAuthenticated");
            logger.info("The user is " +
                    (isAuthenticated ? " with Authenticated! " : "not Authenticated! ") +
                    "The Room ID received is " + roomId +
                    " with userName: " + userData.getString("userName")
            );
            return isAuthenticated;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
        // After handshake logic
    }
}
