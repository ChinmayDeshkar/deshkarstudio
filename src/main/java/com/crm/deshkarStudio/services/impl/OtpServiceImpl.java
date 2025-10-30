package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.model.Otp;
import com.crm.deshkarStudio.repo.OtpRepo;
import com.crm.deshkarStudio.services.OtpService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpRepo otpRepo;

    @Override
    @Transactional
    public void sendOtp(String phoneNumber) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        otpRepo.deleteByPhoneNumber(phoneNumber); // clean old OTP
        otpRepo.save(new Otp(null, phoneNumber, otp, expiry));
        System.out.println(otp);
        //smsService.sendSms(phoneNumber, "Your OTP is: " + otp);
    }

    @Override
    @Transactional
    public boolean verifyOtp(String phoneNumber, String otp) {
        Optional<Otp> record = otpRepo.findByPhoneNumber(phoneNumber);
        if (record.isPresent()) {
            Otp otpRecord = record.get();
            if (otpRecord.getOtp().equals(otp) &&
                    otpRecord.getExpiryTime().isAfter(LocalDateTime.now())) {
                otpRepo.deleteByPhoneNumber(phoneNumber);
                return true;
            }
        }
        return false;
    }
}
