package com.turtlechat.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turtlechat.server.models.dto.ResponseBody;
import com.turtlechat.server.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.OutputStream;

@Component
public class JwtTokenInterceptor implements HandlerInterceptor {
    private final AuthenticationService authService;
    private final ObjectMapper objectMapper;

    public JwtTokenInterceptor(AuthenticationService authService, ObjectMapper objectMapper) {
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        OutputStream out = response.getOutputStream();
        ResponseBody responseBody = new ResponseBody();

        try {
            if(token == null) throw new Exception("Access token is not present");
            JSONObject user = authService.validateUserToken(token);

            if(user.getBoolean("isAuthenticated")) {
                request.setAttribute("userData", user);
                return true;
            } else {
                throw new Exception("Access token is either expired or invalid");
            }
        } catch(Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseBody.setStatus(HttpStatus.UNAUTHORIZED);
            responseBody.setMessage(e.getMessage());
            objectMapper.writeValue(out, responseBody);
            return false;
        }
    }
}
