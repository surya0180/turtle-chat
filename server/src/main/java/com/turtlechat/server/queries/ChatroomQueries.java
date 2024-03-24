package com.turtlechat.server.queries;

import org.springframework.stereotype.Component;

@Component
public class ChatroomQueries {
    public static final String findActiveHostingChatrooms = "SELECT " +
            "cr.room_id as roomId, " +
            "cr.room_name as roomName, " +
            "cr.host_id as hostId, " +
            "ur.user_name as hostName, " +
            "cr.creation_timestamp as creationTimestamp, " +
            "cr.deletion_timestamp as deletionTimestamp, " +
            "cr.room_status as roomStatus, " +
            "COUNT(ucr.user_id) as participantCount " +
            "FROM Chatroom cr " +
            "JOIN User_Chatroom ucr ON cr.room_id = ucr.room_id " +
            "JOIN Users ur ON cr.host_id = ur.user_id " +
            "WHERE cr.host_id = :hostId AND cr.room_status = 'A' " +
            "GROUP BY cr.room_id, cr.room_name, cr.host_id, ur.user_name " +
            "ORDER BY cr.creation_timestamp DESC";

    public static final String findActiveParticipatingChatrooms = "SELECT * FROM (" +
            "SELECT " +
            "cr.room_id as roomId, " +
            "cr.room_name as roomName, " +
            "cr.host_id as hostId, " +
            "ur.user_name as hostName, " +
            "cr.creation_timestamp as creationTimestamp, " +
            "cr.deletion_timestamp as deletionTimestamp, " +
            "cr.room_status as roomStatus, " +
            "COUNT(ucr.user_id) as participantCount " +
            "FROM ChatRoom cr " +
            "JOIN User_ChatRoom ucr ON cr.room_id = ucr.room_id " +
            "JOIN Users ur ON cr.host_id = ur.user_id " +
            "WHERE cr.room_status = 'A' " +
            "GROUP BY cr.room_id, ur.user_name " +
            "ORDER BY cr.creation_timestamp DESC) " +
            "AS tb " +
            "WHERE tb.roomId in (" +
            "SELECT ucr.room_id from user_chatroom ucr " +
            "WHERE ucr.user_id = :userId) " +
            "AND tb.hostId <> :userId";

    public static final String findAllParticipantsInChatroom = "SELECT " +
            "u.user_id as userId, " +
            "u.user_name as userName, " +
            "u.user_email as userEmail " +
            "FROM Users u " +
            "JOIN User_ChatRoom ucr ON u.user_id = ucr.user_id " +
            "WHERE ucr.room_id = :roomId";
}
