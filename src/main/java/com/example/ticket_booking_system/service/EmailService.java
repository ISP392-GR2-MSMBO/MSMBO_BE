package com.example.ticket_booking_system.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}") String from;
    private final JavaMailSender mailSender;
    public void sendEmailVerification(String to, String verifyLink) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from, "Movie Booking"); // hoặc dùng InternetAddress cũng được
            helper.setTo(to);
            helper.setSubject("Verify your email");
            helper.setText(
                    "<p>Nhấn vào link để xác thực:</p><p><a href=\"" + verifyLink + "\">" + verifyLink + "</a></p>",
                    true
            );
            mailSender.send(mimeMessage);
        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            // log lỗi, tránh làm nổ API
            log.error("Send verification email failed", e);
        }
    }

    public void sendPasswordResetEmail(String to, String resetLink) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from, "Movie Booking");
            helper.setTo(to);
            helper.setSubject("Reset Your Password"); // Tiêu đề mới
            helper.setText(
                    "<p>Nhấn vào link để đặt lại mật khẩu:</p><p><a href=\"" + resetLink + "\">" + resetLink + "</a></p>" +
                            "<p>Link này sẽ hết hạn sau 20 phút.</p>",
                    true
            );
            mailSender.send(mimeMessage);
        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            log.error("Send password reset email failed", e);
        }
    }
}
