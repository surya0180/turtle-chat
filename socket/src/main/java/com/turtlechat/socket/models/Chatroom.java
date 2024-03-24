package com.turtlechat.socket.models;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Chatroom {
    private Long roomId;
    private Map<String, WebSocketSession> sessions;

    public Chatroom(Long roomId) {
        this.roomId = roomId;
        this.sessions = new ConcurrentHashMap<>();
    }

    public void setSession(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }

    public void deleteSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public WebSocketSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }
}
