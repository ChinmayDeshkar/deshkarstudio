package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;
    private EmailServiceImpl(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML
            mailSender.send(message);
            log.info("Email sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
        }
    }
}
