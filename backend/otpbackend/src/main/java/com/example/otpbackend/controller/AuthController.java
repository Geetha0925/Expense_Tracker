package com.example.otpbackend.controller;

import com.example.otpbackend.dto.*;
import com.example.otpbackend.model.ApiResponse;
import com.example.otpbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ---------------- SIGNUP ----------------
    @PostMapping("/register")
    public ApiResponse registerUser(@RequestBody RegisterRequest request) {
        return authService.signup(request.getName(), request.getEmail(), request.getPassword());
    }

    @PostMapping("/verify-signup-otp")
    public ApiResponse verifySignupOtp(@RequestBody OtpRequest request) {
        return authService.verifySignupOtp(request.getEmail(), request.getOtp());
    }

    @PostMapping("/resend-signup-otp")
    public ApiResponse resendSignupOtp(@RequestBody OtpRequest request) {
        return authService.resendSignupOtp(request.getEmail());
    }

    // ---------------- LOGIN ----------------
    @PostMapping("/login")
    public ApiResponse loginUser(@RequestBody LoginRequest request) {
        return authService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/verify-login-otp")
    public ApiResponse verifyLoginOtp(@RequestBody OtpRequest request) {
        return authService.verifyLoginOtp(request.getEmail(), request.getOtp());
    }
}
