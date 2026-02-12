package com.example.otpbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // ---------------- SEND OTP EMAIL ----------------
    public void sendOtpEmail(String toEmail, String otp, String type) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Your OTP for BudgetWise AI");

            String text = "Your OTP is: " + otp + "\nIt will expire in 5 minutes.";
            switch (type) {
                case "Signup":
                    text = "Welcome! Your Signup OTP is: " + otp + "\nIt will expire in 5 minutes.";
                    break;
                case "Login":
                    text = "Your Login OTP is: " + otp + "\nIt will expire in 5 minutes.";
                    break;
                case "Password Reset":
                    text = "Your Password Reset OTP is: " + otp + "\nIt will expire in 5 minutes.";
                    break;
            }

            message.setText(text);
            message.setFrom("no-reply@budgetwiseai.com");

            mailSender.send(message);
            System.out.println(type + " OTP sent to " + toEmail + ": " + otp);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
}
