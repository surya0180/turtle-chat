package com.turtlechat.batcher.services;

import com.turtlechat.batcher.models.ChatMessage;
import com.turtlechat.batcher.repositories.MessageRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final Logger logger = LogManager.getLogger(MessageService.class);

    private final MessageRepository messageRepo;

    public MessageService(MessageRepository messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Async("cachedThreadPool")
    public void appendBatchOfMessages(List<ChatMessage> messages, String range) {
        try {
            messageRepo.saveAll(messages);
            messages.forEach(message -> {
                logger.info(message + " This is message");
            });
            logger.info("Saved batch of messages - '{}' - successfully", range);
        } catch(Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }
}
