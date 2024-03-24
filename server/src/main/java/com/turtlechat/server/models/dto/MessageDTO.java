package com.turtlechat.server.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long messageId;
    private String text;
    private Long userId;
    private String userName;
    private Long roomId;
    private Long timestamp;
}
