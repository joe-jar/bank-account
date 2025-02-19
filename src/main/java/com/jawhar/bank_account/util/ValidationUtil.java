package com.jawhar.bank_account.util;

import com.jawhar.bank_account.exception.InvalidAccountException;
import com.jawhar.bank_account.exception.InvalidAmountException;
import com.jawhar.bank_account.model.Account;

import java.math.BigDecimal;

public class ValidationUtil {


    public static  void validateAccount(Account account) {
        if (account == null) {
            throw new InvalidAccountException("Account cannot be null");
        }
    }

    public static void validateAmount(BigDecimal amount, String operation) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(String.format("%s amount must be positive: %s", operation, amount));
        }
    }

}
