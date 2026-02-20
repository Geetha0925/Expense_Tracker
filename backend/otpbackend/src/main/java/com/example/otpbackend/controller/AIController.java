package com.example.otpbackend.controller;

import com.example.otpbackend.service.AIService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/ai-advice")
public String getAdvice(@RequestBody Map<String, Object> payload) {

    String email = payload.get("email").toString();
    double income = Double.parseDouble(payload.get("income").toString());

    return aiService.generateAdvice(email, income);
}


}

