package com.turtlechat.server.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    private final JavaMailSender mailSender;
    private final Logger logger = LogManager.getLogger(EmailSender.class);

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String emailAddress, String otp) throws MailException, MessagingException {
            String subject = "[Turtle Chat] Verify email via OTP";
            String body = String.format("Hi there! %s is your OTP to login to turtle chat. Thanks!", otp);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

            message.setTo(emailAddress);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message.getMimeMessage());
            logger.info("Email sent successfully to: " + emailAddress);
    }
}
