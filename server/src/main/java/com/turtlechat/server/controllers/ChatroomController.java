package com.turtlechat.server.controllers;

import com.turtlechat.server.models.dto.ResponseBody;
import com.turtlechat.server.services.ChatroomService;
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
@RequestMapping("/chatroom")
@CrossOrigin(origins = "*")
public class ChatroomController {
    private final RequestBodyValidator validator;
    private final Logger logger = LogManager.getLogger(ChatroomController.class);
    private final ChatroomService chatroomService;

    public ChatroomController(RequestBodyValidator validator, ChatroomService chatroomService) {
        this.validator = validator;
        this.chatroomService = chatroomService;
    }

    @PostMapping("/create-chatroom")
    public ResponseEntity<ResponseBody> createChatroom(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        JSONObject user = (JSONObject) request.getAttribute("userData");
        ResponseBody response;
        try {
            validator.createChatroomValidation(body);
            Object data = chatroomService.createChatroom(user.getLong("userId"), (String) body.get("roomName"));
            response = new ResponseBody(HttpStatus.CREATED, "Chatroom created successfully", data);
        } catch(Exception e) {
            logger.info(e.getMessage());
            response = new ResponseBody(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update-chatroom")
    public ResponseEntity<ResponseBody> updateChatroom(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        JSONObject user = (JSONObject) request.getAttribute("userData");
        ResponseBody response;
        try {
            validator.updateChatroomValidation(body);
            chatroomService.updateChatroom(
                    user.getLong("userId"),
                    ((Number) body.get("roomId")).longValue(),
                    (String) body.get("roomName")
            );
            response = new ResponseBody(HttpStatus.OK, "Updated chatroom successfully", null);
        } catch(Exception e) {
            logger.info(e.getMessage());
            response = new ResponseBody(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping ("/deactivate-chatroom")
    public ResponseEntity<ResponseBody> deactivateChatroom(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        JSONObject user = (JSONObject) request.getAttribute("userData");
        ResponseBody response;
        try {
            validator.deactivateChatroomValidation(body);
            chatroomService.deactivateChatroom(user.getLong("userId"), ((Number) body.get("roomId")).longValue());
            response = new ResponseBody(HttpStatus.OK, "Deactivated chatroom successfully", null);
        } catch(Exception e) {
            logger.info(e.getMessage());
            response = new ResponseBody(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping ("/exit-chatroom")
    public ResponseEntity<ResponseBody> exitChatroom(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        JSONObject user = (JSONObject) request.getAttribute("userData");
        ResponseBody response;
        try {
            validator.deactivateChatroomValidation(body);
            chatroomService.exitChatroom(user.getLong("userId"), ((Number) body.get("roomId")).longValue());
            response = new ResponseBody(HttpStatus.OK, "Exited chatroom successfully", null);
        } catch(Exception e) {
            logger.info(e);
            response = new ResponseBody(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/hosting-chatrooms")
    public ResponseEntity<ResponseBody> getHostingChatrooms(HttpServletRequest request) {
        JSONObject user = (JSONObject) request.getAttribute("userData");
        ResponseBody response;
        try {
            Object data = chatroomService.findAllHostingChatrooms(user.getLong("userId"));
            response = new ResponseBody(HttpStatus.OK, "Fetched hosting chatrooms successfully!", data);
        } catch(Exception e) {
            logger.info(e.getMessage());
            response = new ResponseBody(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/participating-chatrooms")
    public ResponseEntity<ResponseBody> getParticipatingChatrooms(HttpServletRequest request) {
        JSONObject user = (JSONObject) request.getAttribute("userData");
        ResponseBody response;
        try {
            Object data = chatroomService.findAllParticipatingChatrooms(user.getLong("userId"));
            response = new ResponseBody(HttpStatus.OK, "Fetched participating chatrooms successfully!", data);
        } catch(Exception e) {
            logger.info(e.getMessage());
            response = new ResponseBody(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/participants/{roomId}")
    public ResponseEntity<ResponseBody> getParticipantsInChatroom(HttpServletRequest request, @PathVariable Long roomId) {
        JSONObject user = (JSONObject) request.getAttribute("userData");
        ResponseBody response;
        try {
            Object data = chatroomService.findParticipantsInChatroom(roomId);
            response = new ResponseBody(HttpStatus.OK, "Fetched participants successfully!", data);
        } catch(Exception e) {
            logger.info(e.getMessage());
            response = new ResponseBody(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/messages/{roomId}")
    public ResponseEntity<ResponseBody> getMessagesInChatroom(
            HttpServletRequest request,
            @PathVariable Long roomId,
            @RequestParam(value = "pageNumber") Integer pageNumber,
            @RequestParam("pageLimit") Integer pageLimit
    ) {
        JSONObject user = (JSONObject) request.getAttribute("userData");
        ResponseBody response;
        try {
            Object data = chatroomService.getMessagesInChatroom(roomId, pageNumber, pageLimit);
            response = new ResponseBody(HttpStatus.OK, "Fetched messages successfully!", data);
        } catch(Exception e) {
            logger.info(e.getMessage());
            response = new ResponseBody(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
