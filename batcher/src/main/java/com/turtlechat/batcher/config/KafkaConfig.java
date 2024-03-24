package com.turtlechat.batcher.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Value("${kafka-props.bootstrap-server}")
    private String bootstrapServer;

    @Value("${kafka-props.chat-messages-group}")
    private String chatMessagesGroup;

    @Bean
    public ConsumerFactory<String, String> chatMessageConsumerFactory() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, chatMessagesGroup);
        configMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");
        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configMap.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(configMap);
    }

    // Configuring Kafka Listeners for the consumers using the above factory definitions
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    chatMessageConcurrentKafkaListenerContainerFactory
    (@Autowired ConsumerFactory<String, String>
             chatMessageConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(chatMessageConsumerFactory);
        factory.setAckDiscarded(false);
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setIdleBetweenPolls(3000);
        return factory;
    }
}
