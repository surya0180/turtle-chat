package com.turtlechat.server.repositories;

import com.turtlechat.server.models.dto.InvitationDTO;
import com.turtlechat.server.models.entities.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryRestResource
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Invitation i SET i.inviteStatus = :newInviteStatus WHERE i.inviteId = :inviteId AND i.recipientUserId = :userId")
    int updateInviteStatusByInviteId(
            @Param("userId") Long userId,
            @Param("inviteId") Long inviteId,
            @Param("newInviteStatus") Character newInviteStatus
    );

    @Query(nativeQuery = true)
    List<InvitationDTO> findSentInvitationsByStatus(
            @Param("userId") Long userId,
            @Param("inviteStatus") Character inviteStatus
    );

    @Query(nativeQuery = true)
    List<InvitationDTO> findReceivedInvitationsByStatus(
            @Param("userId") Long userId,
            @Param("inviteStatus") Character inviteStatus
    );

    @Query("SELECT i FROM Invitation i WHERE i.recipientUserId = :recipientId AND i.senderUserId = :senderId AND i.inviteStatus = :inviteStatus")
    Invitation findInvitationBySenderAndRecipient(
            @Param("senderId") Long senderId,
            @Param("recipientId") Long recipientId,
            @Param("inviteStatus") Character inviteStatus
    );
}
