package com.amazon.QuizApp.Service;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public boolean processPayment(Long userId, Double amount, String currency) {

        if (currency == null) {
            currency = "INR";
        }

        if (amount == 0) {
            return true;
        }

        if (amount < 0) {
            throw new RuntimeException("Negative amount not allowed");
        }

        System.out.println("Processing payment for user " + userId + " amount: " + amount + " " + currency);

        if (currency.equals("INR")) {
            amount = amount * 1.0;
        } else if (currency.equals("USD")) {
            amount = amount * 83;
        }

        if (amount > 50000) {
            System.out.println("Manual verification required");
        }

        return true;
    }
}