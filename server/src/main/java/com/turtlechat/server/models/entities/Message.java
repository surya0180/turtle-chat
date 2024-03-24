package com.turtlechat.server.models.entities;

import com.turtlechat.server.models.dto.MessageDTO;
import com.turtlechat.server.queries.MessageQueries;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Message.findByRoomIdOrderByTimestampDesc",
                query = MessageQueries.findByRoomIdOrderByTimestampDesc,
                resultSetMapping = "Mapping.MessageDTO"
        )
})
@SqlResultSetMapping(
        name = "Mapping.MessageDTO",
        classes = @ConstructorResult(
                targetClass = MessageDTO.class,
                columns = {
                        @ColumnResult(name = "messageId"),
                        @ColumnResult(name = "text"),
                        @ColumnResult(name = "userId"),
                        @ColumnResult(name = "userName"),
                        @ColumnResult(name = "roomId"),
                        @ColumnResult(name = "timestamp"),
                }
        )
)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    @Column(length = 5000)
    private String text;
    private Long userId;
    private Long roomId;
    private Long timestamp;

    public Message(String text, Long userId, Long roomId) {
        this.text = text;
        this.userId = userId;
        this.roomId = roomId;
    }
}