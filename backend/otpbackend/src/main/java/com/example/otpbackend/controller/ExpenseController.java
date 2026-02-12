package com.example.otpbackend.controller;

import com.example.otpbackend.model.Expense;
import com.example.otpbackend.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    // ---------------- ADD EXPENSE ----------------
    @PostMapping("/expense")
    public Expense addExpense(@RequestBody Expense expense) {
        return expenseRepository.save(expense);
    }

    // ---------------- GET USER EXPENSES ----------------
    @GetMapping("/expenses/{email}")
    public List<Expense> getExpenses(@PathVariable String email) {
        return expenseRepository.findByUserEmail(email);
    }

    // ---------------- UPDATE EXPENSE ----------------
    @PutMapping("/expense/{id}")
    public Expense updateExpense(@PathVariable String id, @RequestBody Expense expense) {
        expense.setId(id);
        return expenseRepository.save(expense);
    }

    // ---------------- DELETE EXPENSE ----------------
    @DeleteMapping("/expense/{id}")
    public String deleteExpense(@PathVariable String id) {
        expenseRepository.deleteById(id);
        return "Expense deleted";
    }
}
