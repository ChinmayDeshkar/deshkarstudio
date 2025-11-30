package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.model.User;
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

    @Value("${app.mail.from}")
    String mailFrom;
    @Value("${app.mail.to}")
    String mailTo;

    private final JavaMailSender mailSender;
    private EmailServiceImpl(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void createAndSendMailForNewUser(String to, String subject, User user, String password) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("deshkarchinmay42@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            String fullName = user.getFirstName() + " " + user.getLastName();

            String htmlContent =
                    "<div style='font-family: Arial, sans-serif; background-color:#f4f4f7; padding: 20px;'>"
                            + "<div style='max-width:600px; margin:auto; background:white; padding:20px; "
                            + "border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,0.1);'>"

                            + "<h2 style='color:#4CAF50; text-align:center;'>Welcome to Deshkar Studio CRM</h2>"

                            + "<p>Hi <strong>" + fullName + "</strong>,</p>"

                            + "<p>Your employee account has been successfully created. "
                            + "Use the below credentials to log in to the CRM system.</p>"

                            + "<div style='background:#f0f0f0; padding:15px; border-radius:6px; margin-top:10px;'>"
                            + "<p style='margin:0;'><strong>Username:</strong> " + user.getUsername() + "</p>"
                            + "<p style='margin:0;'><strong>Temporary Password:</strong> "
                            + "<span style='color:#d9534f; font-weight:bold;'>" + "'" + password + "'" + "</span></p>"
                            + "</div>"

                            + "<p style='margin-top:20px;'>"
                            + "<strong>Note:</strong> You must change this password on your first login."
                            + "</p>"

                            + "<p>If you face any issues, contact your administrator.</p>"

                            + "<p style='margin-top:25px; text-align:center; font-size:12px; color:#777;'>"
                            + "© 2025 Deshkar Studio CRM — All Rights Reserved"
                            + "</p>"

                            + "</div></div>";

            helper.setText(htmlContent, true);

            mailSender.send(message);

            System.out.println("New user email sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error sending new user email");
        }
    }

    @Override
    public void sendEmail(String to, String subject, String htmlContent) {
        log.info("TO: " + to);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(mailTo);
            helper.setFrom(mailFrom);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // HTML
            mailSender.send(message);
            log.info("Email sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
        }
    }

    public void sendOtpEmail(String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String htmlContent =
                    "<!DOCTYPE html>" +
                            "<html>" +
                            "<head>" +
                            "<style>" +
                            "body {font-family: Arial, sans-serif; color: #333;} " +
                            ".container {max-width: 480px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;} " +
                            ".title {font-size: 20px; font-weight: bold; margin-bottom: 10px; color: #2c3e50;} " +
                            ".otp {font-size: 28px; font-weight: bold; background: #1abc9c; color: #fff; padding: 10px 20px; border-radius: 6px; display: inline-block; margin: 20px 0;} " +
                            ".message {font-size: 16px; margin-bottom: 30px;}" +
                            ".footer {font-size: 13px; color: #777; margin-top: 20px;}" +
                            "</style>" +
                            "</head>" +
                            "<body>" +
                            "<div class='container'>" +
                            "<div class='title'>Your One-Time Password (OTP)</div>" +
                            "<div class='message'>Use the OTP below to complete your login verification:</div>" +
                            "<div class='otp'>" + otp + "</div>" +
                            "<div class='message'>This OTP is valid for the next 5 minutes. Please do not share it with anyone.</div>" +
                            "<div class='footer'>If you didn’t request this email, please ignore it.</div>" +
                            "</div>" +
                            "</body>" +
                            "</html>";

            helper.setTo(mailTo);
            helper.setFrom(mailFrom);
            helper.setSubject("OTP Verification");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email for OTP sent to {} from {}", mailTo, mailFrom);
        } catch (Exception e) {
            log.error("Failed to send OTP email: {}", e.getMessage());
        }
    }

}
