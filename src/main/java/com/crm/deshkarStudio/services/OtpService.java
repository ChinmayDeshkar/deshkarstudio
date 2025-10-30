package com.crm.deshkarStudio.services;

public interface OtpService {

    void sendOtp(String phoneNumber);
    boolean verifyOtp(String phoneNumber, String otp);
}
