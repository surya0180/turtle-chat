package com.turtlechat.socket.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String type;
    private String text;
    private Long userId;
    private String userName;
    private Long roomId;
    private Long timestamp;
}
