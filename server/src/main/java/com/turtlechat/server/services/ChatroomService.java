package com.turtlechat.server.services;

import com.turtlechat.server.models.dto.ChatroomDTO;
import com.turtlechat.server.models.dto.MessageDTO;
import com.turtlechat.server.models.dto.ParticipantDTO;
import com.turtlechat.server.models.entities.Chatroom;
import com.turtlechat.server.models.entities.UserChatroom;
import com.turtlechat.server.repositories.ChatroomRepository;
import com.turtlechat.server.repositories.MessageRepository;
import com.turtlechat.server.repositories.UserChatroomRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatroomService {
    private final Logger logger = LogManager.getLogger(ChatroomService.class);
    private final ChatroomRepository chatroomRepo;
    private final UserChatroomRepository userChatroomRepo;
    private final MessageRepository messageRepo;

    public ChatroomService(ChatroomRepository chatroomRepo, UserChatroomRepository userChatroomRepo, MessageRepository messageRepo) {
        this.chatroomRepo = chatroomRepo;
        this.userChatroomRepo = userChatroomRepo;
        this.messageRepo = messageRepo;
    }

    public Object createChatroom(Long userId, String roomName) throws DataAccessException {
        Chatroom room = chatroomRepo.save(new Chatroom(roomName, userId));
        userChatroomRepo.save(new UserChatroom(userId, room.getRoomId()));

        Map<String, Object> data = new HashMap<>();
        data.put("chatroom", room);
        logger.info("Created chatroom for userId: " + userId + " with roomName: " + roomName);
        return data;
    }

    public void updateChatroom(Long userId, Long roomId, String roomName) throws Exception {
        int numberOfRowsEffected = chatroomRepo.updateRoomNameByRoomId(userId, roomId, roomName);
        logger.info("Updated chatroom for userId: " + userId+  " with roomId: " + roomId + " with newRoomName: " + roomName);
        if(numberOfRowsEffected == 0) throw new Exception("Could not rename the chatroom! Please try after some time");
    }

    public void exitChatroom(Long userId, Long roomId) throws Exception {
        int numberOfRowsEffected = userChatroomRepo.exitRoomByUserIdAndRoomId(userId, roomId);
        logger.info("Exited chatroom for userId: " + userId + " from roomId: " + roomId);
        if(numberOfRowsEffected == 0) throw new Exception("There was an issue while exiting the chatroom!");
    }

    public void deactivateChatroom(Long userId, Long roomId) throws Exception {
        int numberOfRowsEffected = chatroomRepo.deactivateRoomByRoomId(userId, roomId, 'D');
        logger.info("Deactivated chatroom with roomId: " + roomId + " for userId: " + userId);
        if(numberOfRowsEffected == 0) throw new Exception("There was an issue while deactivating the chatroom!");
    }

    public Object findAllHostingChatrooms(Long hostId) {
        List<ChatroomDTO> rooms = chatroomRepo.findActiveHostingChatrooms(hostId);
        Map<String, Object> data = new HashMap<>();
        data.put("rooms", rooms);
        logger.info("Fetched all hosting chatrooms for hostId: " + hostId);
        return data;
    }

    public Object findAllParticipatingChatrooms(Long userId) {
        List<ChatroomDTO> rooms = chatroomRepo.findActiveParticipatingChatrooms(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("rooms", rooms);
        logger.info("Fetched all participating chatrooms for userId: " + userId);
        return data;
    }

    public Object findParticipantsInChatroom(Long roomId) {
        List<ParticipantDTO> users = userChatroomRepo.findParticipantsByRoomId(roomId);
        Map<String, Object> data = new HashMap<>();
        data.put("users", users);
        logger.info("Fetched all participants in chatroom with roomId: " + roomId);
        return data;
    }

    public Map<String, Object> getMessagesInChatroom(Long roomId, Integer pageNumber, Integer pageLimit) {
        List<MessageDTO> messages = messageRepo.findByRoomIdOrderByTimestampDesc(roomId, pageNumber, pageLimit);
        logger.info(messages);
        Map<String, Object> data = new HashMap<>();
        data.put("messages", messages);
        logger.info("Fetched all messages in chatroom with roomId: " + roomId + " in page "+pageNumber + " with limit " + pageLimit);
        return data;
    }
}
