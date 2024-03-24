package com.turtlechat.socket.handlers;

import com.google.gson.Gson;
import com.turtlechat.socket.models.ChatMessage;
import com.turtlechat.socket.redis.RedisMessagePublisher;
import com.turtlechat.socket.services.ChatroomService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Instant;

@Component
public class ChatroomHandler extends TextWebSocketHandler {
    @Value("${kafka-props.chat-messages-topic}")
    private String chatMessageTopic;
    @Value("${server-props.server-name}")
    private String serverName;
    private final Logger logger = LogManager.getLogger(ChatroomHandler.class);
    private final Gson gson;
    private final ChatroomService chatroomService;
    private final RedisMessagePublisher redisPub;
    private final KafkaTemplate<String, String> kafkaProducer;

    public ChatroomHandler(Gson gson, ChatroomService chatroomService, RedisMessagePublisher redisPub,
            KafkaTemplate<String, String> kafkaProducer) {
        this.gson = gson;
        this.chatroomService = chatroomService;
        this.redisPub = redisPub;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long roomId = (Long) session.getAttributes().get("roomId");
        JSONObject userData = (JSONObject) session.getAttributes().get("userData");
        logger.info(userData.getString("userName") + " is connected in room with room ID: " + roomId + " in server "
                + serverName);
        chatroomService.addSessionToChatroom(roomId, session);
        this.publishMessage(roomId, new ChatMessage(
                "N",
                String.format("%s joined the chat!", userData.getString("userName")),
                userData.getLong("userId"),
                userData.getString("userName"),
                roomId,
                Instant.now().toEpochMilli()
        ));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long roomId = (Long) session.getAttributes().get("roomId");
        JSONObject userData = (JSONObject) session.getAttributes().get("userData");
        logger.info(userData.getString("userName") + " is disconnected in room with room ID: " + roomId + " in server "
                + serverName);
        chatroomService.removeSessionFromChatroom(roomId, session.getId());
        this.publishMessage(roomId, new ChatMessage(
                "N",
                String.format("%s left the chat!", userData.getString("userName")),
                userData.getLong("userId"),
                userData.getString("userName"),
                roomId,
                Instant.now().toEpochMilli()
        ));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            JSONObject userData = (JSONObject) session.getAttributes().get("userData");

            Long roomId = (Long) session.getAttributes().get("roomId");
            Long userId = userData.getLong("userId");
            String userName = userData.getString("userName");

            logger.info(message.getPayload());

            ChatMessage chatMessage = gson.fromJson(message.getPayload(), ChatMessage.class);
            chatMessage.setUserId(userId);
            chatMessage.setUserName(userName);
            chatMessage.setRoomId(roomId);
            chatMessage.setTimestamp(Instant.now().toEpochMilli());

            publishMessage(roomId, chatMessage);

            logger.info("Handled Text Message in sessionId: " + session.getId() + " with roomId: " + roomId + " for userId: " + userId + " with userName: " + userName);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception occurred Handling Text Message in sessionId: " + session.getId() + " :: " + e.getMessage());
        }
    }

    @Async("cachedThreadPool")
    public void publishMessage(Long roomId, ChatMessage chatMessage) {
        redisPub.publish(roomId, chatMessage);
        if(chatMessage.getType() == null) {
            kafkaProducer.send(chatMessageTopic, gson.toJson(chatMessage, ChatMessage.class));
        }
    }
}
