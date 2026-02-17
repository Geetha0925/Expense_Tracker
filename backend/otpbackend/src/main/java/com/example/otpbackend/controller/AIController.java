package com.example.otpbackend.controller;

import com.example.otpbackend.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AIController {

    @Autowired
    private AIService aiService;

    @GetMapping("/ai-advice/{email}")
public String getAdvice(@PathVariable String email,
                        @RequestParam double income) {
    return aiService.generateAdvice(email, income);
}

}

