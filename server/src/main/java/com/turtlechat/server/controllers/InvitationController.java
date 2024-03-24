package com.turtlechat.server.controllers;

import com.turtlechat.server.models.dto.ResponseBody;
import com.turtlechat.server.services.InvitationService;
import com.turtlechat.server.utils.RequestBodyValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/invitation")
@CrossOrigin(origins = "*")
public class InvitationController {
    private final Logger logger = LogManager.getLogger(InvitationController.class);
    private final RequestBodyValidator validator;
    private final InvitationService invitationService;

    public InvitationController(RequestBodyValidator validator, InvitationService invitationService) {
        this.validator = validator;
        this.invitationService = invitationService;
    }

    @GetMapping("/sent-invitations/{inviteStatus}")
    public ResponseEntity<ResponseBody> getSentInvitations(@PathVariable String inviteStatus, HttpServletRequest request) {
        JSONObject user = (JSONObject) request.getAttribute("userData");
        ResponseBody response;
        try {
            Object data = invitationService.getSentInvitations(user.getLong("userId"), inviteStatus.charAt(0));
            response = new ResponseBody(HttpStatus.OK, "Fetched sent invitations successfully", data);
        } catch(Exception e) {
            logger.info(e.getMessage());
            response = new ResponseBody(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/received-invitations/{inviteStatus}")
    public ResponseEntity<ResponseBody> getReceivedInvitations(@PathVariable String inviteStatus, HttpServletRequest request) {
        JSONObject user = (JSONObject) request.getAttribute("userData");
        ResponseBody response;
        try {
            Object data = invitationService.getReceivedInvitations(user.getLong("userId"), inviteStatus.charAt(0));
            response = new ResponseBody(HttpStatus.OK, "Fetched sent invitations successfully", data);
        } catch(Exception e) {
            logger.info(e.getMessage());
            response = new ResponseBody(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/invite-user")
    public ResponseEntity<ResponseBody> inviteUserByUserEmail(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        JSONObject user = (JSONObject) request.getAttribute("userData");
        ResponseBody response;

        try {
            validator.inviteUserByEmailValidation(body);
            invitationService.inviteUserByUserEmail(
                    user.getLong("userId"),
                    (String) body.get("recipientEmail"),
                    ((Number) body.get("roomId")).longValue()
            );
            response = new ResponseBody(HttpStatus.OK, "Invited " + (String) body.get("recipientEmail") + " successfully", null);
        } catch(Exception e) {
            logger.info(e.getMessage());
            response = new ResponseBody(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update-invite")
    public ResponseEntity<ResponseBody> updateInvite(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        JSONObject user = (JSONObject) request.getAttribute("userData");
        ResponseBody response;

        try {
            validator.updateInviteValidation(body);
            invitationService.updateInvitation(
                    user.getLong("userId"),
                    ((Number) body.get("inviteId")).longValue(),
                    ((String) body.get("inviteStatus")).charAt(0)
            );
            response = new ResponseBody(HttpStatus.OK, "Invitation updated successfully", null);
        } catch(Exception e) {
            logger.info(e.getMessage());
            response = new ResponseBody(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
