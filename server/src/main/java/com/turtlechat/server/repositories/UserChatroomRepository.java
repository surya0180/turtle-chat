package com.turtlechat.server.repositories;

import com.turtlechat.server.models.dto.ParticipantDTO;
import com.turtlechat.server.models.entities.UserChatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryRestResource
public interface UserChatroomRepository extends JpaRepository<UserChatroom, Long> {
    UserChatroom findByUserIdAndRoomId(@Param("userId") Long userId, @Param("roomId") Long roomId);

    @Query(nativeQuery = true)
    List<ParticipantDTO> findParticipantsByRoomId(@Param("roomId") Long roomId);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserChatroom uc WHERE uc.userId = :userId AND uc.roomId = :roomId")
    int exitRoomByUserIdAndRoomId(@Param("userId") Long userId, @Param("roomId") Long roomId);
}
