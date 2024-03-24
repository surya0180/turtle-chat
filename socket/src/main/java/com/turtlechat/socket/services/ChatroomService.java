package com.turtlechat.socket.services;

import com.google.gson.Gson;
import com.turtlechat.socket.models.ChatMessage;
import com.turtlechat.socket.models.Chatroom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatroomService {
    @Value("${server-props.server-name}")
    private String serverName;
    private final Map<Long, Chatroom> chatrooms = new ConcurrentHashMap<>();
    private final Logger logger = LogManager.getLogger(ChatroomService.class);

    private final Gson gson;

    public ChatroomService(Gson gson) {
        this.gson = gson;
    }

    public Map<Long, Chatroom> getChatrooms() {
        return chatrooms;
    }

    public void addSessionToChatroom(Long roomId, WebSocketSession session) {
        Chatroom room = chatrooms.getOrDefault(roomId, new Chatroom(roomId));
        room.setSession(session);
        chatrooms.put(roomId, room);
        logger.info("Session with ID: " + session.getId() + " is added to room with ID: " + roomId + " successfully");
    }

    public void removeSessionFromChatroom(Long roomId, String sessionId) {
        Chatroom room = chatrooms.get(roomId);
        room.deleteSession(sessionId);
        chatrooms.put(roomId, room);
        logger.info("Session with ID: " + sessionId + " is removed from room with ID: " + roomId + " successfully");
    }

    @Async("cachedThreadPool")
    public void sendMessageToChatroom(ChatMessage message, Long roomId) throws Exception {
        Chatroom room = chatrooms.getOrDefault(roomId, null);
        if (room == null) {
            throw new Exception(roomId + " is not present in " + serverName + " server");
        }
        Map<String, WebSocketSession> sessions = room.getSessions();

        for (var session : sessions.entrySet()) {
            try {
                session.getValue().sendMessage(new TextMessage(gson.toJson(message)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
