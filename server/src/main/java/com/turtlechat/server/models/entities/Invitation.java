package com.turtlechat.server.models.entities;

import com.turtlechat.server.models.dto.InvitationDTO;
import com.turtlechat.server.queries.InvitationQueries;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Invitation.findSentInvitationsByStatus",
                query = InvitationQueries.findSentInvitationsByStatus,
                resultSetMapping = "Mapping.InvitationDTO"
        ),
        @NamedNativeQuery(
                name = "Invitation.findReceivedInvitationsByStatus",
                query = InvitationQueries.findReceivedInvitationsByStatus,
                resultSetMapping = "Mapping.InvitationDTO"
        )
})
@SqlResultSetMapping(
        name = "Mapping.InvitationDTO",
        classes = @ConstructorResult(
                targetClass = InvitationDTO.class,
                columns = {
                        @ColumnResult(name = "inviteId"),
                        @ColumnResult(name = "senderUserId"),
                        @ColumnResult(name = "senderUserName"),
                        @ColumnResult(name = "recipientUserId"),
                        @ColumnResult(name = "recipientUserName"),
                        @ColumnResult(name = "roomId"),
                        @ColumnResult(name = "roomName"),
                        @ColumnResult(name = "inviteStatus"),
                        @ColumnResult(name = "inviteTimestamp"),
                }
        )
)

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invitation")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inviteId;
    private Long senderUserId;
    private Long recipientUserId;
    private Long roomId;
    private Character inviteStatus;
    private Long inviteTimestamp;

    public Invitation(Long senderUserId, Long recipientUserId, Long roomId) {
        this.senderUserId = senderUserId;
        this.recipientUserId = recipientUserId;
        this.roomId = roomId;
        this.inviteStatus = PENDING;
        this.inviteTimestamp = Instant.now().toEpochMilli();
    }

    public static final Character PENDING = 'P';
    public static final Character ACCEPTED = 'A';
    public static final Character REJECTED = 'R';
}
