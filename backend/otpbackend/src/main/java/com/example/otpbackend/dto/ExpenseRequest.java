package com.example.otpbackend.dto;

public class ExpenseRequest {
    public String name;
    public double amount;
    public String category;
    public String date;
    public String userEmail;
    
    public String getName() { return name; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getUserEmail() { return userEmail; }
}
