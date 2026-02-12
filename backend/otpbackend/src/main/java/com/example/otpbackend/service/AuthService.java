package com.example.otpbackend.service;

import com.example.otpbackend.model.User;
import com.example.otpbackend.model.ApiResponse;
import com.example.otpbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // ---------------- Helper to generate OTP ----------------
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }

    // ---------------- Helper to check OTP expiry ----------------
    private boolean isOtpValid(LocalDateTime otpTime) {
        if (otpTime == null) return false;
        LocalDateTime now = LocalDateTime.now();
        return otpTime.plusMinutes(5).isAfter(now); // valid for 5 minutes
    }

    // ---------------- SIGNUP ----------------
    public ApiResponse signup(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            return new ApiResponse(false, "Email already exists");
        }

        String otp = generateOtp();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password); // TODO: Encrypt password in production
        user.setSignupOtp(otp);
        user.setSignupOtpTime(LocalDateTime.now());
        user.setVerified(false);

        userRepository.save(user);

        // Send OTP email
        emailService.sendOtpEmail(email, otp, "Signup");

        return new ApiResponse(true, "Signup successful. OTP sent to email.");
    }

    public ApiResponse verifySignupOtp(String email, String otp) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return new ApiResponse(false, "User not found");

        User user = userOpt.get();

        if (!isOtpValid(user.getSignupOtpTime())) {
            return new ApiResponse(false, "OTP expired. Please resend OTP.");
        }

        if (user.getSignupOtp() != null && user.getSignupOtp().equals(otp)) {
            user.setVerified(true);
            user.setSignupOtp(null);
            user.setSignupOtpTime(null);
            userRepository.save(user);
            return new ApiResponse(true, "Signup verified successfully");
        } else {
            return new ApiResponse(false, "Invalid OTP");
        }
    }

    public ApiResponse resendSignupOtp(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return new ApiResponse(false, "User not found");

        User user = userOpt.get();
        String otp = generateOtp();
        user.setSignupOtp(otp);
        user.setSignupOtpTime(LocalDateTime.now());
        userRepository.save(user);

        emailService.sendOtpEmail(email, otp, "Signup");

        return new ApiResponse(true, "Signup OTP resent to email.");
    }

    // ---------------- LOGIN ----------------
    public ApiResponse login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return new ApiResponse(false, "User not found");

        User user = userOpt.get();

        if (!user.isVerified()) return new ApiResponse(false, "Email not verified");
        if (!user.getPassword().equals(password)) return new ApiResponse(false, "Incorrect password");

        String otp = generateOtp();
        user.setLoginOtp(otp);
        user.setLoginOtpTime(LocalDateTime.now());
        userRepository.save(user);

        emailService.sendOtpEmail(email, otp, "Login");

        return new ApiResponse(true, "Password verified. OTP sent to email.");
    }

    public ApiResponse verifyLoginOtp(String email, String otp) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return new ApiResponse(false, "User not found");

        User user = userOpt.get();

        if (!isOtpValid(user.getLoginOtpTime())) {
            return new ApiResponse(false, "OTP expired. Please login again.");
        }

        if (user.getLoginOtp() != null && user.getLoginOtp().equals(otp)) {
            user.setLoginOtp(null);
            user.setLoginOtpTime(null);
            userRepository.save(user);
            return new ApiResponse(true, "Login successful");
        } else {
            return new ApiResponse(false, "Invalid OTP");
        }
    }

    // ---------------- FORGOT PASSWORD ----------------
    public ApiResponse forgotPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return new ApiResponse(false, "User not found");

        User user = userOpt.get();
        String otp = generateOtp();
        user.setForgotPasswordOtp(otp);
        user.setForgotPasswordOtpTime(LocalDateTime.now());
        userRepository.save(user);

        emailService.sendOtpEmail(email, otp, "Password Reset");

        return new ApiResponse(true, "OTP sent to email for password reset.");
    }

    public ApiResponse verifyForgotPasswordOtp(String email, String otp) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return new ApiResponse(false, "User not found");

        User user = userOpt.get();

        if (!isOtpValid(user.getForgotPasswordOtpTime())) {
            return new ApiResponse(false, "OTP expired. Please request a new OTP.");
        }

        if (user.getForgotPasswordOtp() != null && user.getForgotPasswordOtp().equals(otp)) {
            return new ApiResponse(true, "OTP verified. You can reset your password.");
        } else {
            return new ApiResponse(false, "Invalid OTP");
        }
    }

    public ApiResponse resetPassword(String email, String otp, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return new ApiResponse(false, "User not found");

        User user = userOpt.get();

        if (!isOtpValid(user.getForgotPasswordOtpTime())) {
            return new ApiResponse(false, "OTP expired. Please request a new OTP.");
        }

        if (user.getForgotPasswordOtp() != null && user.getForgotPasswordOtp().equals(otp)) {
            user.setPassword(newPassword);  // TODO: Encrypt password in production
            user.setForgotPasswordOtp(null);
            user.setForgotPasswordOtpTime(null);
            userRepository.save(user);
            return new ApiResponse(true, "Password reset successfully");
        } else {
            return new ApiResponse(false, "Invalid OTP");
        }
    }
}
