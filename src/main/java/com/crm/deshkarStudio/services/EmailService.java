package com.crm.deshkarStudio.services;

public interface EmailService {

    void sendEmail(String to, String subject, String htmlContent);
}
