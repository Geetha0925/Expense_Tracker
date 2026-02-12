package com.example.otpbackend.service;

import com.example.otpbackend.model.Expense;
import com.example.otpbackend.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repo;

    public Expense save(Expense expense) {
        return repo.save(expense);
    }

    public List<Expense> getByEmail(String email) {
        return repo.findByUserEmail(email);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
