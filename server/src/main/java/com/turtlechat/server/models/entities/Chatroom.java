package com.turtlechat.server.models.entities;

import com.turtlechat.server.models.dto.ChatroomDTO;
import com.turtlechat.server.queries.ChatroomQueries;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NamedNativeQueries({
    @NamedNativeQuery(
        name = "Chatroom.findActiveHostingChatrooms",
        query = ChatroomQueries.findActiveHostingChatrooms,
        resultSetMapping = "Mapping.ChatroomDTO"
    ),
    @NamedNativeQuery(
        name = "Chatroom.findActiveParticipatingChatrooms",
        query = ChatroomQueries.findActiveParticipatingChatrooms,
        resultSetMapping = "Mapping.ChatroomDTO"
    )
})
@SqlResultSetMapping(
    name = "Mapping.ChatroomDTO",
    classes = @ConstructorResult(
        targetClass = ChatroomDTO.class,
        columns = {
            @ColumnResult(name = "roomId"),
            @ColumnResult(name = "roomName"),
            @ColumnResult(name = "hostId"),
            @ColumnResult(name = "hostName"),
            @ColumnResult(name = "creationTimestamp"),
            @ColumnResult(name = "deletionTimestamp"),
            @ColumnResult(name = "roomStatus"),
            @ColumnResult(name = "participantCount"),
        }
    )
)

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chatroom")
public class Chatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;
    private String roomName;
    private Long hostId;
    private Long creationTimestamp;
    private Long deletionTimestamp;
    @Column(columnDefinition = "CHAR(1) DEFAULT 'A'")
    private char roomStatus;
    @Transient
    private Long participantCount;

    public Chatroom(String roomName, Long hostId) {
        this.roomName = roomName;
        this.hostId = hostId;
        this.creationTimestamp = Instant.now().toEpochMilli();
        this.roomStatus = 'A';
    }
}
