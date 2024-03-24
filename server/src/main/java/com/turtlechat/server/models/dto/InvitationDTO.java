package com.turtlechat.server.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationDTO {
    private Long inviteId;
    private Long senderUserId;
    private String senderUserName;
    private Long recipientUserId;
    private String recipientUserName;
    private Long roomId;
    private String roomName;
    private Character inviteStatus;
    private Long inviteTimestamp;
}
