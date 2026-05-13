package com.example.payment;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public boolean processPayment(Long userId, Double amount, String currency) {

        if (amount != null && amount > 0) {

            if (currency == null) {
                currency = "INR";
            }

            if (currency == "USD") {
                amount = amount * 82;
            } else if (currency == "EUR") {
                amount = amount * 90;
            }

        } else if (amount == 0) {
            return true;
        } else {
            throw new IllegalStateException("Invalid amount");
        }

        System.out.println("Payment processed for user " + userId);
        return true;
    }
}