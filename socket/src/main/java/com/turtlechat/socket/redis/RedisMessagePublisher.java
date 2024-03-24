package com.turtlechat.socket.redis;

import com.google.gson.Gson;
import com.turtlechat.socket.models.ChatMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class RedisMessagePublisher{
    private final Logger logger = LogManager.getLogger(RedisMessagePublisher.class);
    private final RedisTemplate<String, Object> redisTemplate;
    private final Gson gson;

    public RedisMessagePublisher(RedisTemplate<String, Object> redisTemplate, Gson gson) {
        this.redisTemplate = redisTemplate;
        this.gson = gson;
    }

    @Async("cachedThreadPool")
    public void publish(Long roomId, ChatMessage message) {
        try {
            redisTemplate.convertAndSend(
                    new ChannelTopic(String.format("chatroom-%d", roomId)).getTopic(),
                    gson.toJson(message, ChatMessage.class)
            );
            logger.info("Published message to Room: " + roomId + " Successfully from user with userId " + message.getUserId());
        } catch(Exception e) {
            e.printStackTrace();
            logger.info("Failed to publish message to Room: " + roomId + " from user with userId " + message.getUserId() + " !" + e.getMessage());
        }
    }
}
