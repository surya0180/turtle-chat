package com.turtlechat.server.queries;

import org.springframework.stereotype.Component;

@Component
public class InvitationQueries {
    public static final String findSentInvitationsByStatus = "SELECT " +
            "i.invite_id AS inviteId, " +
            "i.room_id AS roomId, " +
            "c.room_name AS roomName, " +
            "i.sender_user_id AS senderUserId, " +
            "s.user_name AS senderUserName, " +
            "i.recipient_user_id AS recipientUserId, " +
            "r.user_name AS recipientUserName, " +
            "i.invite_status AS inviteStatus, " +
            "i.invite_timestamp AS inviteTimestamp FROM " +
            "invitation i " +
            "JOIN chatroom c ON i.room_id = c.room_id " +
            "JOIN users s ON i.sender_user_id = s.user_id " +
            "JOIN users r ON i.recipient_user_id = r.user_id " +
            "WHERE i.sender_user_id = :userId AND i.invite_status = :inviteStatus " +
            "ORDER BY i.invite_timestamp DESC";
    public static final String findReceivedInvitationsByStatus = "SELECT " +
            "i.invite_id AS inviteId, " +
            "i.room_id AS roomId, " +
            "c.room_name AS roomName, " +
            "i.sender_user_id AS senderUserId, " +
            "s.user_name AS senderUserName, " +
            "i.recipient_user_id AS recipientUserId, " +
            "r.user_name AS recipientUserName, " +
            "i.invite_status AS inviteStatus, " +
            "i.invite_timestamp AS inviteTimestamp FROM " +
            "invitation i " +
            "JOIN chatroom c ON i.room_id = c.room_id " +
            "JOIN users s ON i.sender_user_id = s.user_id " +
            "JOIN users r ON i.recipient_user_id = r.user_id " +
            "WHERE i.recipient_user_id = :userId AND i.invite_status = :inviteStatus " +
            "ORDER BY i.invite_timestamp DESC";
}
