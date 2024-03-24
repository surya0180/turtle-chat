package com.turtlechat.server.repositories;

import com.turtlechat.server.models.dto.MessageDTO;
import com.turtlechat.server.models.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(nativeQuery = true)
    List<MessageDTO> findByRoomIdOrderByTimestampDesc(
        @Param("roomId") Long roomId,
        @Param("pageNumber") Integer pageNumber,
        @Param("pageLimit") Integer pageLimit
    );
}
