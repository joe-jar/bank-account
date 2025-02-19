package com.jawhar.bank_account.util;

import com.jawhar.bank_account.model.Account;
import com.jawhar.bank_account.model.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

@Slf4j
public class TransactionFormatterUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#,##0.00");

    public static String generateStatement(Account account) {
        if (account.transactions().isEmpty()) {
            log.warn("Account ID {} has no transactions. Statement will be empty.", account.id());
            return "No transactions available for this account.";
        }

        StringJoiner statement = new StringJoiner("\n");
        statement.add("=========================================");
        statement.add("Bank Statement for Account ID: " + account.id());
        statement.add("=========================================");
        statement.add("Date & Time          | Type      | Amount     | Balance   ");
        statement.add("-------------------------------------------------------");

        for (Transaction transaction : account.transactions()) {
            statement.add(formatTransaction(transaction));
        }

        statement.add("=========================================");
        statement.add("End of statement.");

        return statement.toString();
    }

    public static String formatTransaction(Transaction transaction) {
        return String.format("%-19s | %-9s | %10s | %10s",
                transaction.transactionTime().format(DATE_FORMATTER),
                transaction.type(),
                DECIMAL_FORMATTER.format(transaction.amount()),
                DECIMAL_FORMATTER.format(transaction.postTransactionBalance()));
    }
}
