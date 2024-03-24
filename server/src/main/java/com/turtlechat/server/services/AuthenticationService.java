package com.turtlechat.server.services;

import com.turtlechat.server.controllers.AuthController;
import com.turtlechat.server.models.entities.User;
import com.turtlechat.server.repositories.UserRepository;
import com.turtlechat.server.utils.EmailSender;
import com.turtlechat.server.utils.TokenValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.mail.MessagingException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthenticationService {
    private final TokenValidator tokenValidator;
    private final EmailSender emailSender;
    private final UserRepository userRepo;
    private final RedisTemplate<String, Object> redisTemplate;

    private final Logger logger = LogManager.getLogger(AuthController.class);

    public AuthenticationService(TokenValidator tokenValidator, EmailSender emailSender, UserRepository userRepo, RedisTemplate<String, Object> template) {
        this.tokenValidator = tokenValidator;
        this.emailSender = emailSender;
        this.userRepo = userRepo;
        this.redisTemplate = template;
    }

    private static String generateOTP() {
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    @Async("cachedThreadPool")
    public void saveToRedis(String hash, String key, Object value) {
        redisTemplate.opsForHash().put(hash, key, value);
    }

    public void deleteFromRedis(String hash, String key) {
        redisTemplate.opsForHash().delete(hash, key);
    }

    public void sendOTP(String emailAddress) throws MailException, MessagingException {
        String otp = generateOTP();
        saveToRedis("EMAIL_OTPS", emailAddress, otp);
        logger.info("The otp sent to: " + emailAddress + " is = " + otp);
        emailSender.sendEmail(emailAddress, otp);
    }

    public Pair<Object, String> verifyOTP(String emailAddress, String OTP) throws Exception {
        String redisOtp = (String) redisTemplate.opsForHash().get("EMAIL_OTPS", emailAddress);
        boolean status = redisOtp != null && (redisOtp.equals(OTP));

        if(!status) throw new Exception("OTP verification failed! " +
                "Looks like the OTP has expired or an incorrect OTP is entered! " +
                "Please request for a new OTP to verify your email"
        );

        deleteFromRedis("EMAIL_OTPS", emailAddress);
        String type = "E";
        User user = userRepo.findByEmail(emailAddress);
        Map<String, Object> response = new HashMap<>();
        if(user == null) {
            type = "C";
            user = userRepo.save(new User("", emailAddress));
        } else {
            response.put("accessToken", tokenValidator.generateJWTToken(user));
        }


        response.put("user", user);
        logger.info("Email address verified successfully for: " + emailAddress);
        return Pair.of(response, type);
    }

    public Object saveUserName(Long userId, String userName, String userEmail) throws Exception {
        logger.info(userId +" " + userName + " " + userEmail);
        int numberOfRowsEffected = userRepo.updateUsernameByEmail(userEmail, userName);
        if(numberOfRowsEffected == 0) throw new Exception("Failed to update username! " +
                "No matching record found for the given email address"
        );
        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", tokenValidator.generateJWTToken(new User(userId, userName, userEmail)));
        logger.info("Saved username for userId: " + userId + " and userEmail: " + userEmail);
        return response;
    }

    public JSONObject validateUserToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException {
        Claims claims = tokenValidator.validateJWTToken(token);
        JSONObject userData = new JSONObject();
        if(claims != null) {
            userData.put("userId", claims.get("userId"))
                    .put("userName", claims.get("userName"))
                    .put("userEmail", claims.get("userEmail"))
                    .put("isAuthenticated", true);
        } else {
            userData.put("isAuthenticated", false);
        }
        return userData;
    }
}
