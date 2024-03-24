package com.turtlechat.server.services;

import com.turtlechat.server.models.dto.InvitationDTO;
import com.turtlechat.server.models.entities.Invitation;
import com.turtlechat.server.models.entities.User;
import com.turtlechat.server.models.entities.UserChatroom;
import com.turtlechat.server.repositories.InvitationRepository;
import com.turtlechat.server.repositories.UserChatroomRepository;
import com.turtlechat.server.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InvitationService {
    private final Logger logger = LogManager.getLogger(InvitationService.class);

    private final UserRepository userRepo;
    private final InvitationRepository invitationRepo;
    private final UserChatroomRepository userChatroomRepo;

    public InvitationService(UserRepository userRepo, InvitationRepository invitationRepo, UserChatroomRepository userChatroomRepo) {
        this.userRepo = userRepo;
        this.invitationRepo = invitationRepo;
        this.userChatroomRepo = userChatroomRepo;
    }

    public Object getSentInvitations(Long userId, Character inviteStatus) throws DataAccessException {
        Map<String, Object> data = new HashMap<>();
        List<InvitationDTO> invitations = invitationRepo.findSentInvitationsByStatus(userId, inviteStatus);
        data.put("invitations", invitations);
        logger.info("Fetched sent invitation with inviteStatus: " + inviteStatus + " for userId: " + userId);
        return data;
    }

    public Object getReceivedInvitations(Long userId, Character inviteStatus) throws DataAccessException {
        Map<String, Object> data = new HashMap<>();
        List<InvitationDTO> invitations = invitationRepo.findReceivedInvitationsByStatus(userId, inviteStatus);
        data.put("invitations", invitations);
        logger.info("Fetched received invitation with inviteStatus: " + inviteStatus + " for userId: " + userId);
        return data;
    }

    public void inviteUserByUserEmail(Long userId, String recipientEmail, Long roomId) throws Exception {
        User user = userRepo.findByEmail(recipientEmail);
        if(user == null) {
            throw new Exception("User with email: " + recipientEmail + " does not exist");
        } else {
            UserChatroom userChatroom = userChatroomRepo.findByUserIdAndRoomId(user.getUserId(), roomId);
            if (userChatroom != null) {
                throw new Exception("This user is already a participant in this room");
            }
            Invitation invitation = invitationRepo.findInvitationBySenderAndRecipient(userId, user.getUserId(), 'P');
            if (invitation != null) {
                throw new Exception("This user has already been invited! Wait for the user to accept the invitation");
            }
            logger.info("invited user with email address " + recipientEmail + " for userId: " + userId + " in chatroom with roomId: " + roomId);
            invitationRepo.save(new Invitation(userId, user.getUserId(), roomId));
        }
    }

    public void updateInvitation(Long userId, Long inviteId, Character inviteStatus) throws Exception {
        Optional<Invitation> invitation = invitationRepo.findById(inviteId);
        int numberOfRowsEffected = invitationRepo.updateInviteStatusByInviteId(userId, inviteId, inviteStatus);
        if(!inviteStatus.equals(Invitation.PENDING) && invitation.isPresent()) {
            if(invitation.get().getInviteStatus().equals(Invitation.PENDING)) {
                userChatroomRepo.save(new UserChatroom(userId, invitation.get().getRoomId()));
            } else {
                throw new Exception("This invitation has already been actioned");
            }
            logger.info("Updated invitation with inviteId: " + inviteId + " with inviteStatus: " + inviteStatus + " by userId: " + userId);
        }
        if(numberOfRowsEffected == 0) throw new Exception("Invalid invitation!");
    }
}
