package com.turtlechat.server.controllers;

import com.turtlechat.server.models.dto.ResponseBody;
import com.turtlechat.server.services.AuthenticationService;
import com.turtlechat.server.utils.RequestBodyValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/authentication")
@CrossOrigin(origins = "*")
public class AuthController {
    private final RequestBodyValidator validator;
    private final AuthenticationService authService;
    private final Logger logger = LogManager.getLogger(AuthController.class);

    public AuthController(RequestBodyValidator validator, AuthenticationService authService) {
        this.validator = validator;
        this.authService = authService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ResponseBody> sendOTP(@RequestBody Map<String, Object> body) {
        ResponseBody response;
        try {
            validator.sendOtpValidation(body);
            String email = (String) body.get("userEmail");
            authService.sendOTP(email);
            response = new ResponseBody(HttpStatus.OK, "OTP sent to " + email + " successfully", null);
        } catch(Exception e) {
            logger.info(e.getMessage());
            response = new ResponseBody(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ResponseBody> verifyOTP(@RequestBody Map<String, Object> body) {
        ResponseBody response;
        try {
            validator.verifyOtpValidation(body);
            Pair<Object, String> data = authService.verifyOTP((String) body.get("userEmail"), (String) body.get("OTP"));
            response = new ResponseBody(
                    data.getRight().equals("E") ? HttpStatus.OK : HttpStatus.CREATED,
                    "Email verified successfully!",
                    data.getLeft()
            );
        } catch(Exception e) {
            logger.info(e.getMessage());
            response = new ResponseBody(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping ("/save-username")
    public ResponseEntity<ResponseBody> saveUserName(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        ResponseBody response;
        try {
            validator.saveUserNameValidation(body);
            Object data = authService.saveUserName(((Number) body.get("userId")).longValue(), (String) body.get("userName"), (String) body.get("userEmail"));
            response = new ResponseBody(HttpStatus.OK, "Updated username successfully", data);
        } catch(Exception e) {
            logger.info(e.getMessage());
            response = new ResponseBody(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
