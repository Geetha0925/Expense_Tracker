package com.example.otpbackend.service;

import com.example.otpbackend.model.Expense;
import com.example.otpbackend.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public Map<String, Object> generateFinancialSummary(String email) {

        List<Expense> expenses = expenseRepository.findByUserEmail(email);

        Map<String, Object> result = new HashMap<>();

        double totalExpense = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        // Category totals
        Map<String, Double> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));

        LocalDate now = LocalDate.now();
        Month currentMonth = now.getMonth();
        Month previousMonth = currentMonth.minus(1);

        double currentMonthTotal = 0;
        double previousMonthTotal = 0;

        for (Expense e : expenses) {

            if (e.getDate() == null || e.getDate().isEmpty()) continue;

            LocalDate expenseDate = LocalDate.parse(e.getDate());
            Month expenseMonth = expenseDate.getMonth();

            if (expenseMonth == currentMonth) {
                currentMonthTotal += e.getAmount();
            }

            if (expenseMonth == previousMonth) {
                previousMonthTotal += e.getAmount();
            }
        }

        int daysPassed = now.getDayOfMonth();
        int totalDays = now.lengthOfMonth();

        double predictedMonthTotal = 0;
        if (daysPassed > 0) {
            predictedMonthTotal = (currentMonthTotal / daysPassed) * totalDays;
        }

        result.put("totalExpense", totalExpense);
        result.put("categoryTotals", categoryTotals);
        result.put("currentMonthTotal", currentMonthTotal);
        result.put("previousMonthTotal", previousMonthTotal);
        result.put("predictedMonthTotal", predictedMonthTotal);

        return result;
    }
}
