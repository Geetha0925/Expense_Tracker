package com.example.otpbackend.controller;

import com.example.otpbackend.dto.*;
import com.example.otpbackend.model.ApiResponse;
import com.example.otpbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class PasswordController {

    @Autowired
    private AuthService authService;

    @PostMapping("/forgot-password")
    public ApiResponse forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return authService.forgotPassword(request.getEmail());
    }

    @PostMapping("/verify-forgot-password-otp")
    public ApiResponse verifyForgotPasswordOtp(@RequestBody OtpRequest request) {
        return authService.verifyForgotPasswordOtp(request.getEmail(), request.getOtp());
    }

    @PostMapping("/reset-password")
    public ApiResponse resetPassword(@RequestBody ResetPasswordRequest request) {
        return authService.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
    }
}
