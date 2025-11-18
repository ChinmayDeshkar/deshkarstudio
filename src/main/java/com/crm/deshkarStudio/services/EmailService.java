package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.model.User;

public interface EmailService {

    void sendEmail(String to, String subject, String htmlContent);

    void createAndSendMailForNewUser(String to, String subject, User user, String password);

    void sendOtpEmail(String otp);

}
