package com.jawhar.bank_account.service;

import com.jawhar.bank_account.exception.InvalidAccountException;
import com.jawhar.bank_account.model.Account;
import com.jawhar.bank_account.model.Transaction;
import com.jawhar.bank_account.model.TransactionType;
import com.jawhar.bank_account.util.TransactionFormatterUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    private Account account;

    @BeforeEach
    void setup() {
        account = new Account(12345L, BigDecimal.ZERO, List.of());
    }

    @Test
    void testGenerateStatement_WithTransactions() {
        // ✅ Define transactions dynamically
        List<Transaction> transactions = List.of(
                new Transaction(LocalDateTime.of(2024, 2, 18, 16, 5, 10), TransactionType.DEPOSIT, new BigDecimal("1000.00"), new BigDecimal("1000.00")),
                new Transaction(LocalDateTime.of(2024, 2, 18, 16, 10, 30), TransactionType.WITHDRAWAL, new BigDecimal("-300.75"), new BigDecimal("699.25"))
        );

        account = new Account(account.id(), account.balance(), transactions);

        // ✅ Generate the actual statement
        String actualStatement = transactionService.printStatement(account);

        // ✅ Assert that the statement contains key elements dynamically
        assertTrue(actualStatement.contains("Bank Statement for Account ID: 12345"), "Statement should include the account ID.");
        assertTrue(actualStatement.contains("Date & Time"), "Statement should contain column headers.");
        assertTrue(actualStatement.contains("DEPOSIT"), "Statement should include deposit transactions.");
        assertTrue(actualStatement.contains("WITHDRAWAL"), "Statement should include withdrawal transactions.");
        assertTrue(actualStatement.contains("1,000.00"), "Statement should correctly format amounts.");
        assertTrue(actualStatement.contains("-300.75"), "Statement should correctly format negative transactions.");
        assertTrue(actualStatement.contains("End of statement."), "Statement should have a proper ending.");
    }

    @Test
    void testGenerateStatement_NoTransactions() {
        String actualStatement = transactionService.printStatement(account);

        // Only check if it correctly states "no transactions"
        assertEquals("No transactions available for this account.", actualStatement, "Statement should correctly indicate no transactions.");
    }

    @Test
    void testGenerateStatement_NullAccount_ShouldThrowInvalidAccountException() {
        Exception exception = assertThrows(InvalidAccountException.class, () -> {
            transactionService.printStatement(null);
        });

        assertEquals("Account cannot be null.", exception.getMessage(), "Exception message should indicate null account.");
    }
}
