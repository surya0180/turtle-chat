package com.turtlechat.server.repositories;

import com.turtlechat.server.models.dto.ChatroomDTO;
import com.turtlechat.server.models.entities.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryRestResource
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Chatroom c SET c.roomName = :newRoomName WHERE c.roomId = :roomId AND c.hostId = :userId")
    int updateRoomNameByRoomId(
            @Param("userId") Long userId,
            @Param("roomId") Long roomId,
            @Param("newRoomName") String newRoomName
    );

    @Transactional
    @Modifying
    @Query("UPDATE Chatroom c SET c.roomStatus = :newRoomStatus WHERE c.roomId = :roomId AND c.hostId = :userId")
    int deactivateRoomByRoomId(
            @Param("userId") Long userId,
            @Param("roomId") Long roomId,
            @Param("newRoomStatus") Character newRoomStatus
    );

    @Query(nativeQuery = true)
    List<ChatroomDTO> findActiveHostingChatrooms(@Param("hostId") Long hostId);

    @Query(nativeQuery = true)
    List<ChatroomDTO> findActiveParticipatingChatrooms(@Param("userId") Long userId);
}
