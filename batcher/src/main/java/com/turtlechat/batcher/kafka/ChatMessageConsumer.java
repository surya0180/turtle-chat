package com.turtlechat.batcher.kafka;

import com.google.gson.Gson;
import com.turtlechat.batcher.models.ChatMessage;
import com.turtlechat.batcher.services.MessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChatMessageConsumer {
    private final Logger logger = LogManager.getLogger(ChatMessageConsumer.class);
    private final Gson gson;
    private final MessageService messageService;

    public ChatMessageConsumer(Gson gson, MessageService messageService) {
        this.gson = gson;
        this.messageService = messageService;
    }

    @KafkaListener(topics = "${kafka-props.chat-messages-topic}", groupId = "${kafka-props.chat-messages-group}", containerFactory = "chatMessageConcurrentKafkaListenerContainerFactory")
    @Async("cachedThreadPool")
    void consume(@Payload List<String> messages,
                 @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                 @Header(KafkaHeaders.OFFSET) List<Long> offsets,
                 Acknowledgment acknowledgment
    ) {
        try {
            logger.info("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
            logger.info("Starting the process to receive batch messages");

            List<ChatMessage> batchMessages = new ArrayList<>();

            for (int i = 0; i < messages.size(); i++) {
                try {
                    ChatMessage message = gson.fromJson(messages.get(i), ChatMessage.class);
                    batchMessages.add(message);
                } catch(Exception e) {
                    logger.info("Error while parsing message at partition-offset='{}'",partitions.get(i) + "-" + offsets.get(i));
                }

                logger.info("received message with partition-offset='{}'",partitions.get(i) + "-" + offsets.get(i));
            }

            messageService.appendBatchOfMessages(
                    batchMessages,
                    String.format("%d-%d to %d-%d",
                            partitions.get(0), offsets.get(0),
                            partitions.get(partitions.size()-1), offsets.get(offsets.size()-1)
            ));
            logger.info("all the batch messages are consumed");
        } catch (Exception e) {
            logger.info("Unable to parse message, Error: " + e.getMessage());
        }
        acknowledgment.acknowledge();
    }
}
