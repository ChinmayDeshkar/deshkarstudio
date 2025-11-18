package com.crm.deshkarStudio.services;

public interface OtpService {

    String sendOtp(String phoneNumber);
    boolean verifyOtp(String phoneNumber, String otp);
}
