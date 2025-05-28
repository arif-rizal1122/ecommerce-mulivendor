package com.ecommerce.ecommerce_multivendor.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException; // Import ini
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException; // Import ini
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendVerificationOtpEmail(String userEmail, String otp, String subject, String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");

            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true); 
            mimeMessageHelper.setTo(userEmail);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new MailSendException("Failed to prepare or send email due to messaging error: " + e.getMessage(), e);
        } catch (org.springframework.mail.MailException e) {
            throw new MailSendException("Failed to send email: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new MailSendException("An unexpected error occurred while sending email: " + e.getMessage(), e);
        }
    }
}