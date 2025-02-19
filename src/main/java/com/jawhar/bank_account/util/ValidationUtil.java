package com.jawhar.bank_account.util;

import com.jawhar.bank_account.exception.InvalidAccountException;
import com.jawhar.bank_account.exception.InvalidAmountException;
import com.jawhar.bank_account.model.Account;

import java.math.BigDecimal;

public final class ValidationUtil {

    private ValidationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    public static void validateAccount(Account account) {
        if (account == null) {
            throw new InvalidAccountException("Account cannot be null.");
        }

        if (account.id() == null) {
            throw new InvalidAccountException("Account ID cannot be null.");
        }

        if (account.balance().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAccountException("Account balance cannot be negative.");
        }
    }

    public static void validateAmount(BigDecimal amount, String operation) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(operation + " amount must be positive: " + amount); // ✅ More efficient than String.format()
        }
    }
}
