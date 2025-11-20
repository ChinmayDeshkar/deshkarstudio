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
            helper.setTo("itschinmayd@gmail.com");
            helper.setFrom("deshkarchinmay42@gmail.com");
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // HTML
            mailSender.send(message);
            log.info("Email sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
        }
    }

    public void sendOtpEmail(String otp) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo("itschinmayd@gmail.com");
            helper.setFrom("deshkarchinmay42@gmail.com");
            helper.setSubject("OTP for login");
            helper.setText("OTP for your login: " + otp, true); // HTML
            mailSender.send(message);
            log.info("Email for OTP sent to {}", "itschinmayd@gmail.com");
        }
        catch (Exception e){
            log.error(e.toString());
        }
    }
}
