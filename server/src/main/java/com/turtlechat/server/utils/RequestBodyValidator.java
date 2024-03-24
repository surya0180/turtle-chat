package com.turtlechat.server.utils;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RequestBodyValidator {
    private void validateEmail(String email) throws InvalidRequestBodyException {
        Pattern pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()) throw new InvalidRequestBodyException("Invalid Email Address");
    }

    public void sendOtpValidation(Map<String, Object> body) throws InvalidRequestBodyException {
        if(!body.containsKey("userEmail")) throw new InvalidRequestBodyException("userEmail is required in request body!");
        validateEmail((String) body.get("userEmail"));
    }

    public void verifyOtpValidation(Map<String, Object> body) throws InvalidRequestBodyException {
        this.sendOtpValidation(body);
        if(!body.containsKey("OTP")) throw new InvalidRequestBodyException("OTP is required in request body!");
        String otp = (String) body.get("OTP");
        if(otp == null || otp.length() != 6 || !otp.matches("\\d{6}")) throw new InvalidRequestBodyException("Invalid OTP sent");
    }

    public void saveUserNameValidation(Map<String, Object> body) throws InvalidRequestBodyException {
        if(!body.containsKey("userName")) throw new InvalidRequestBodyException("userName is required in request body");
        String userName = (String) body.get("userName");
        if(userName == null || userName.length() == 0) throw new InvalidRequestBodyException("Invalid userName!");
    }

    public void createChatroomValidation(Map<String, Object> body) throws InvalidRequestBodyException {
        if(!body.containsKey("roomName")) throw new InvalidRequestBodyException("roomName is required in request body");
    }

    public void updateChatroomValidation(Map<String, Object> body) throws InvalidRequestBodyException {
        if(!body.containsKey("roomId")) throw new InvalidRequestBodyException("roomId is required in request body");
        if(!body.containsKey("roomName")) throw new InvalidRequestBodyException("roomName is required in request body");

        try {
            Long roomId = ((Number) body.get("roomId")).longValue();
            String roomName = (String) body.get("roomName");

            if(roomName == null || roomName.length() == 0) throw new InvalidRequestBodyException("Invalid roomName passed!");
        } catch(Exception e) {
            throw new InvalidRequestBodyException(e.getMessage());
        }
    }

    public void deactivateChatroomValidation(Map<String, Object> body) throws InvalidRequestBodyException {
        if(!body.containsKey("roomId")) throw new InvalidRequestBodyException("roomId is required in request body");
        try {
            ((Number) body.get("roomId")).longValue();
        } catch(Exception e) {
            throw new InvalidRequestBodyException(e.getMessage());
        }
    }

    public void inviteUserByEmailValidation(Map<String, Object> body) throws InvalidRequestBodyException {
        if(!body.containsKey("recipientEmail")) throw new InvalidRequestBodyException("recipientEmail is required in request body");
        validateEmail((String) body.get("recipientEmail"));
        this.deactivateChatroomValidation(body);
    }

    public void updateInviteValidation(Map<String, Object> body) throws InvalidRequestBodyException {
        if(!body.containsKey("inviteId")) throw new InvalidRequestBodyException("inviteId is required field");
        if(!body.containsKey("inviteStatus")) throw new InvalidRequestBodyException("inviteStatus is required field");

        try {
            ((Number) body.get("inviteId")).longValue();
        } catch(Exception e) {
            throw new InvalidRequestBodyException(e.getMessage());
        }
    }

    public static class InvalidRequestBodyException extends Exception {
        public InvalidRequestBodyException(String message) {
            super(message);
        }
    }
}
