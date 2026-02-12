package com.example.otpbackend.service;

import com.example.otpbackend.model.Otp;
import com.example.otpbackend.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    public String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    public void sendOtp(String email) {
    String otp = generateOtp();

    otpRepository.deleteByEmail(email);

    Otp otpEntity = new Otp();
    otpEntity.setEmail(email);
    otpEntity.setOtp(otp);
    otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

    otpRepository.save(otpEntity);

    // Pass the type for email
    emailService.sendOtpEmail(email, otp, "Generic OTP");
}


    public boolean verifyOtp(String email, String otp) {
        return otpRepository.findByEmail(email)
                .filter(o -> o.getOtp().equals(otp))
                .filter(o -> o.getExpiryTime().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}
