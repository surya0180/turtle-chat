package com.turtlechat.server.queries;

import org.springframework.stereotype.Component;

@Component
public class MessageQueries {
    public static final String findByRoomIdOrderByTimestampDesc = "SELECT " +
            "m.message_id as messageId, " +
            "m.text as text, " +
            "m.user_id as userId, " +
            "u.user_name as userName, " +
            "m.room_id as roomId, " +
            "m.timestamp as timestamp " +
            "FROM message m " +
            "JOIN users u ON m.user_id = u.user_id " +
            "WHERE m.room_id = :roomId " +
            "ORDER BY m.timestamp " +
            "LIMIT :pageLimit " +
            "OFFSET (:pageNumber - 1) * :pageLimit";
}
