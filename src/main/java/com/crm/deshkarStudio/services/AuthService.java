package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.dto.LoginRequest;
import com.crm.deshkarStudio.dto.SignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface AuthService {


    // STEP 1: Request OTP for signup
    public ResponseEntity<?> requestSignupOtp(SignupRequest req) ;

    // STEP 2: Verify OTP & register
    public ResponseEntity<?> verifySignupOtp(String phone, String otp) ;

    // LOGIN OTP FLOW
    public ResponseEntity<?> requestLoginOtp(String phone) ;

    public ResponseEntity<?> verifyLoginOtp(String phone, String otp) ;
}
