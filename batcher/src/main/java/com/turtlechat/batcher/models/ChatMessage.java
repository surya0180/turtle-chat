package com.turtlechat.batcher.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    @Column(length = 5000)
    private String text;
    private Long userId;
    private Long roomId;
    private Long timestamp;

    public ChatMessage(String text, Long userId, Long roomId) {
        this.text = text;
        this.userId = userId;
        this.roomId = roomId;
    }
}
