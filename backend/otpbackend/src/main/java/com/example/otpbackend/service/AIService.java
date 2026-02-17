package com.example.otpbackend.service;

import com.example.otpbackend.model.Expense;
import com.example.otpbackend.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AIService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public String generateAdvice(String email, double income) {

        List<Expense> expenses = expenseRepository.findByUserEmail(email);

        if (expenses == null || expenses.isEmpty()) {
            return "No expenses found. Add expenses to receive AI insights.";
        }

        if (income <= 0) {
            return "Income must be greater than zero to generate AI insights.";
        }

        double total = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        double remaining = income - total;

        StringBuilder report = new StringBuilder();
        report.append("🤖 AI SMART FINANCIAL REPORT\n\n");

        /* ================= CATEGORY ANALYSIS ================= */

        Map<String, Double> categoryTotals = new HashMap<>();

        for (Expense e : expenses) {
            categoryTotals.put(
                    e.getCategory(),
                    categoryTotals.getOrDefault(e.getCategory(), 0.0) + e.getAmount()
            );
        }

        report.append("📊 Category Breakdown:\n");

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {

            double percentage = (entry.getValue() / total) * 100;

            report.append("- ")
                    .append(entry.getKey())
                    .append(": ₹")
                    .append(String.format("%.2f", entry.getValue()))
                    .append(" (")
                    .append(String.format("%.1f", percentage))
                    .append("%)\n");

            if (percentage > 30) {
                report.append("  ⚠ High spending in ")
                        .append(entry.getKey())
                        .append(" category!\n");
            }
        }

        /* ================= MONTHLY TREND ================= */

        Map<String, Double> monthlyTotals = new HashMap<>();

        for (Expense e : expenses) {
            String month = e.getDate().substring(0, 7);
            monthlyTotals.put(
                    month,
                    monthlyTotals.getOrDefault(month, 0.0) + e.getAmount()
            );
        }

        List<String> sortedMonths = new ArrayList<>(monthlyTotals.keySet());
        Collections.sort(sortedMonths);

        double predictedNextMonth;

        if (sortedMonths.size() >= 2) {
            double last = monthlyTotals.get(sortedMonths.get(sortedMonths.size() - 1));
            double prev = monthlyTotals.get(sortedMonths.get(sortedMonths.size() - 2));

            if (prev != 0) {
                double growthRate = (last - prev) / prev;
                predictedNextMonth = last + (last * growthRate);
            } else {
                predictedNextMonth = last;
            }
        } else {
            predictedNextMonth = total * 1.1; // assume 10% increase
        }

        report.append("\n🔮 Monthly Trend Prediction:\n");
        report.append("Estimated next month spending: ₹")
                .append(String.format("%.2f", predictedNextMonth))
                .append("\n");

        /* ================= ANOMALY DETECTION ================= */

        report.append("\n🚨 Anomaly Detection:\n");

        double averageExpense = total / expenses.size();
        boolean anomalyFound = false;

        for (Expense e : expenses) {
            if (e.getAmount() > averageExpense * 2) {
                anomalyFound = true;
                report.append("- High expense detected: ")
                        .append(e.getName())
                        .append(" (₹")
                        .append(String.format("%.2f", e.getAmount()))
                        .append(")\n");
            }
        }

        if (!anomalyFound) {
            report.append("No unusual expenses detected.\n");
        }

        /* ================= SIMPLE AI SCORE ================= */

        double score = 100 - ((total / income) * 100);

        if (score < 0) score = 0;
        if (score > 100) score = 100;

        report.append("\n💰 Budget Health:\n");

        if (remaining < income * 0.2) {
            report.append("⚠ Low remaining balance. Try to save more.\n");
        } else {
            report.append("Your budget usage is under control.\n");
        }

        report.append("\n🏆 AI Budget Score: ")
                .append(String.format("%.0f", score))
                .append("/100\n");

        if (score >= 80) {
            report.append("Excellent financial management!\n");
        } else if (score >= 60) {
            report.append("Good financial condition.\n");
        } else if (score >= 40) {
            report.append("Moderate risk. Monitor your spending.\n");
        } else {
            report.append("High financial risk. Reduce expenses.\n");
        }

        report.append("\n📉 Financial Risk Level: ");

        if (score >= 75) report.append("LOW\n");
        else if (score >= 50) report.append("MEDIUM\n");
        else report.append("HIGH\n");

        /* ================= DATA-BASED AI INSIGHTS ================= */

        report.append("\n💡 AI Insights:\n");

        boolean suggestionAdded = false;

        // Category based suggestions
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            double percentage = (entry.getValue() / total) * 100;

            if (percentage > 30) {
                report.append("- Consider reducing expenses in ")
                        .append(entry.getKey())
                        .append(" category.\n");
                suggestionAdded = true;
            }
        }

        // High total spending
        if (total > income * 0.8) {
            report.append("- Your total spending is close to your income limit.\n");
            suggestionAdded = true;
        }

        // Increasing trend
        if (predictedNextMonth > total) {
            report.append("- Spending trend is increasing. Monitor next month's expenses.\n");
            suggestionAdded = true;
        }

        if (!suggestionAdded) {
            report.append("Your spending pattern looks balanced.\n");
        }

        return report.toString();
    }
}

