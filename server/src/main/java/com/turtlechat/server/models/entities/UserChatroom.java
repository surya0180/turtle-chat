package com.turtlechat.server.models.entities;

import com.turtlechat.server.models.dto.ChatroomDTO;
import com.turtlechat.server.models.dto.ParticipantDTO;
import com.turtlechat.server.queries.ChatroomQueries;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NamedNativeQueries({
    @NamedNativeQuery(
        name = "UserChatroom.findParticipantsByRoomId",
        query = ChatroomQueries.findAllParticipantsInChatroom,
        resultSetMapping = "Mapping.ParticipantDTO"
    )
})
@SqlResultSetMapping(
    name = "Mapping.ParticipantDTO",
    classes = @ConstructorResult(
        targetClass = ParticipantDTO.class,
        columns = {
            @ColumnResult(name = "userId"),
            @ColumnResult(name = "userName"),
            @ColumnResult(name = "userEmail")
        }
    )
)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_chatroom")
public class UserChatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mapId;
    private Long userId;
    private Long roomId;

    public UserChatroom(Long userId, Long roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }
}
