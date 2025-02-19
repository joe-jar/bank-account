package com.jawhar.bank_account.model;

import java.math.BigDecimal;
import java.util.List;

public record Account(Long id, BigDecimal balance, List<Transaction> transactions) {

    public Account(Long id, BigDecimal balance, List<Transaction> transactions) {
        this.id = id;
        this.balance = (balance != null) ? balance : BigDecimal.ZERO; // Ensure non-null balance
        this.transactions = (transactions != null) ? List.copyOf(transactions) : List.of(); // Ensure immutability
    }
}