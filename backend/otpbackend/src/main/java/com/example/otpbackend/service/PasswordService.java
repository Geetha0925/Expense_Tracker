package com.example.otpbackend.service;

import com.example.otpbackend.model.User;
import com.example.otpbackend.model.ApiResponse;
import com.example.otpbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    // ---------------- FORGOT PASSWORD (Send OTP) ----------------
    public ApiResponse forgotPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return new ApiResponse(false, "User not found");
        }

        // Send OTP to user email
        otpService.sendOtp(email);

        return new ApiResponse(true, "OTP sent to your email");
    }

    // ---------------- VERIFY FORGOT PASSWORD OTP ----------------
    public ApiResponse verifyForgotPasswordOtp(String email, String otp) {
        boolean valid = otpService.verifyOtp(email, otp);

        if (!valid) return new ApiResponse(false, "Invalid or expired OTP");

        return new ApiResponse(true, "OTP verified, you can reset your password");
    }

    // ---------------- RESET PASSWORD ----------------
    public ApiResponse resetPassword(String email, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return new ApiResponse(false, "User not found");
        }

        User user = userOpt.get();
        user.setPassword(newPassword); // In production, hash the password!
        userRepository.save(user);

        return new ApiResponse(true, "Password reset successfully");
    }
}
