package com.example.otpbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String name;
    private String email;
    private String password;
    private boolean verified;

    // OTPs
    private String signupOtp;
    private LocalDateTime signupOtpTime;

    private String loginOtp;
    private LocalDateTime loginOtpTime;

    private String forgotPasswordOtp;
    private LocalDateTime forgotPasswordOtpTime;

    // ---------------- Getters ----------------
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isVerified() { return verified; }
    public String getSignupOtp() { return signupOtp; }
    public LocalDateTime getSignupOtpTime() { return signupOtpTime; }
    public String getLoginOtp() { return loginOtp; }
    public LocalDateTime getLoginOtpTime() { return loginOtpTime; }
    public String getForgotPasswordOtp() { return forgotPasswordOtp; }
    public LocalDateTime getForgotPasswordOtpTime() { return forgotPasswordOtpTime; }

    // ---------------- Setters ----------------
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public void setSignupOtp(String signupOtp) { this.signupOtp = signupOtp; }
    public void setSignupOtpTime(LocalDateTime signupOtpTime) { this.signupOtpTime = signupOtpTime; }
    public void setLoginOtp(String loginOtp) { this.loginOtp = loginOtp; }
    public void setLoginOtpTime(LocalDateTime loginOtpTime) { this.loginOtpTime = loginOtpTime; }
    public void setForgotPasswordOtp(String forgotPasswordOtp) { this.forgotPasswordOtp = forgotPasswordOtp; }
    public void setForgotPasswordOtpTime(LocalDateTime forgotPasswordOtpTime) { this.forgotPasswordOtpTime = forgotPasswordOtpTime; }
}
