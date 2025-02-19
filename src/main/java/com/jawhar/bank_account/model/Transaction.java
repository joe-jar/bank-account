package com.jawhar.bank_account.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public record Transaction(LocalDateTime transactionTime, TransactionType type,
                          BigDecimal amount, BigDecimal postTransactionBalance) {}




