package com.turtlechat.socket.redis;

import com.google.gson.Gson;
import com.turtlechat.socket.models.ChatMessage;
import com.turtlechat.socket.services.ChatroomService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisMessageSubscriber implements MessageListener {
    private final Logger logger = LogManager.getLogger(RedisMessageSubscriber.class);
    private final ChatroomService chatroomService;
    private final Gson gson;

    public RedisMessageSubscriber(ChatroomService chatroomService, Gson gson) {
        this.chatroomService = chatroomService;
        this.gson = gson;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(message.getChannel());
            Long roomId = Long.parseLong(channel.split("-")[1]);

            ChatMessage chatMessage = gson.fromJson(new String(message.getBody()), ChatMessage.class);
            chatroomService.sendMessageToChatroom(chatMessage, roomId);
            logger.info(chatMessage + " received from channel: " + channel);

            logger.info("Message sent to chatroom with id: " + roomId + " successfully");
        } catch(Exception e) {
            e.printStackTrace();
            logger.info("Failed to send message to chatroom! " + e.getMessage());
        }
    }
}
