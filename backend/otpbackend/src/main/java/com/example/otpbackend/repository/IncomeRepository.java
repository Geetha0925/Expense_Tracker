package com.example.otpbackend.repository;

import com.example.otpbackend.model.Income;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IncomeRepository extends MongoRepository<Income, String> {

    Income findByUserEmail(String userEmail);
}
