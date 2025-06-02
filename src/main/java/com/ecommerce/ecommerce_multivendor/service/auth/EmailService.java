package com.ecommerce.ecommerce_multivendor.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // Import ini
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    // Inject alamat email pengirim dari konfigurasi Spring Mail
    @Value("${spring.mail.username}") // Ambil dari spring.mail.username di application.properties
    private String senderEmail;

    public void sendVerificationOtpEmail(String userEmail, String otp, String subject, String text, String frontend_url) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");

            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);
            mimeMessageHelper.setTo(userEmail.trim()); // Tambahkan .trim() untuk berjaga-jaga
            mimeMessageHelper.setFrom(senderEmail); // <--- PERBAIKAN UTAMA DI SINI

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            // Log error lebih detail jika perlu
            throw new MailSendException("Failed to prepare or send email due to messaging error: " + e.getMessage(), e);
        } catch (org.springframework.mail.MailException e) {
            // Log error lebih detail jika perlu
            throw new MailSendException("Failed to send email: " + e.getMessage(), e);
        } catch (Exception e) {
            // Log error lebih detail jika perlu
            throw new MailSendException("An unexpected error occurred while sending email: " + e.getMessage(), e);
        }
    }
}