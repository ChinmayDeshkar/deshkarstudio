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
    private String mailFrom;
    @Value("${app.mail.to}")
    private String[] mailTo;
    @Value("${app.mail.cc}")
    private String[] mailCC;

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

    public void sendOtpEmail(String otp, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8" />
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 0;
                            padding: 0;
                            background: #f4f6f8;
                        }
                        .container {
                            max-width: 480px;
                            background: #ffffff;
                            margin: 30px auto;
                            padding: 25px 30px;
                            border-radius: 10px;
                            box-shadow: 0px 4px 10px rgba(0,0,0,0.08);
                        }
                        .title {
                            font-size: 22px;
                            font-weight: 600;
                            margin-bottom: 10px;
                            color: #2c3e50;
                        }
                        .username {
                            font-size: 16px;
                            margin-bottom: 15px;
                            color: #444;
                        }
                        .otp-box {
                            font-size: 32px;
                            font-weight: bold;
                            padding: 15px;
                            text-align: center;
                            background: #007bff;
                            color: #ffffff;
                            border-radius: 8px;
                            margin: 25px 0;
                            letter-spacing: 5px;
                        }
                        .info {
                            font-size: 15px;
                            color: #555;
                            margin-bottom: 25px;
                            line-height: 1.5;
                        }
                        .footer {
                            font-size: 12px;
                            color: #777;
                            text-align: center;
                            margin-top: 20px;
                            border-top: 1px solid #eee;
                            padding-top: 10px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="title">Login Verification Required</div>
                        <div class="username">Hi <b>Ashay/Prashant</b>,</div>
                        <div class="info">
                            We received a login request for %s account. Use the OTP below to proceed with secure access:
                        </div>
                        <div class="otp-box">%s</div>
                        <div class="info">
                            This OTP is valid only for the next <b>5 minutes</b>.
                            Please do not share it with anyone for security reasons.
                        </div>
                        <div class="footer">
                            If you did not initiate this request, please ignore this email or contact support immediately.
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(username, otp);

            helper.setTo(mailTo);
            helper.setFrom(mailFrom);
            helper.setCc(mailCC);

            helper.setSubject("OTP Login Verification - Secure Access");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("OTP Email sent to {} (User: {})", mailTo, username);

        } catch (Exception e) {
            log.error("Failed to send OTP email:", e);
        }
    }


}
