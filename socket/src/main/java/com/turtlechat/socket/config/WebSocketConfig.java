package com.turtlechat.socket.config;

import com.turtlechat.socket.handlers.ChatroomHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatroomHandler chatroomHandler;

    private final JwtTokenInterceptor jwtTokenInterceptor;

    public WebSocketConfig(ChatroomHandler chatroomHandler, JwtTokenInterceptor jwtTokenInterceptor) {
        this.chatroomHandler = chatroomHandler;
        this.jwtTokenInterceptor = jwtTokenInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatroomHandler, "/chatroom")
                .addInterceptors(jwtTokenInterceptor)
                .setAllowedOriginPatterns("*");
    }
}
