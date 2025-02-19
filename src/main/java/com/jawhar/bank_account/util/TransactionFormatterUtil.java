package com.jawhar.bank_account.util;

import com.jawhar.bank_account.model.Account;
import com.jawhar.bank_account.model.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
public final class TransactionFormatterUtil { // ✅ Marked as final (utility class)

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#,##0.00;-#,##0.00");

    private TransactionFormatterUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    public static String generateStatement(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }

        List<Transaction> transactions = account.transactions();
        if (transactions.isEmpty()) {
            log.warn("Account ID {} has no transactions. Statement will be empty.", account.id());
            return "No transactions available for this account.";
        }

        return new StringJoiner("\n")
                .add("=========================================")
                .add(String.format("Bank Statement for Account ID: %d", account.id()))
                .add("=========================================")
                .add("Date & Time          | Type      | Amount     | Balance   ")
                .add("-------------------------------------------------------")
                .add(transactions.stream()
                        .map(TransactionFormatterUtil::formatTransaction)
                        .collect(Collectors.joining("\n"))) // ✅ Efficiently join formatted transactions
                .add("=========================================")
                .add("End of statement.")
                .toString();
    }

    private static String formatTransaction(Transaction transaction) {
        return String.format("%-19s | %-9s | %10s | %10s",
                transaction.transactionTime().format(DATE_FORMATTER),
                transaction.type(),
                DECIMAL_FORMATTER.format(transaction.amount()),
                DECIMAL_FORMATTER.format(transaction.postTransactionBalance()));
    }
}
