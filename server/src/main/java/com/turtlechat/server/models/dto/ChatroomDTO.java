package com.turtlechat.server.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomDTO {
    private Long roomId;
    private String roomName;
    private Long hostId;
    private String hostName;
    private Long creationTimestamp;
    private Long deletionTimestamp;
    private char roomStatus;
    private Long participantCount;
}
