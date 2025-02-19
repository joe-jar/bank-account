package com.jawhar.bank_account.service;

import com.jawhar.bank_account.exception.InvalidAccountException;
import com.jawhar.bank_account.model.Account;
import com.jawhar.bank_account.model.Transaction;
import com.jawhar.bank_account.model.TransactionType;
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
        // Given: Initialize an account with no transactions
        account = new Account(12345L, BigDecimal.ZERO, List.of());
    }

    @Test
    void testGenerateStatement_WithTransactions() {
        // Given: A list of transactions (deposit & withdrawal)
        List<Transaction> transactions = List.of(
                new Transaction(LocalDateTime.of(2024, 2, 18, 16, 5, 10), TransactionType.DEPOSIT, new BigDecimal("100.00"), new BigDecimal("100.00")),
                new Transaction(LocalDateTime.of(2024, 2, 18, 16, 10, 30), TransactionType.WITHDRAWAL, new BigDecimal("30.75"), new BigDecimal("69.25"))
        );
        account = new Account(account.id(), new BigDecimal("69.25"), transactions); // ✅ Final balance is now correct

        // When: The statement is generated
        String actualStatement = transactionService.printStatement(account);

        // Then: Validate that the statement contains the expected details
        assertAll(
                () -> assertTrue(actualStatement.contains("Bank Statement for Account ID: 12345"), "Statement should include account ID."),
                () -> assertTrue(actualStatement.contains("Date & Time"), "Statement should contain column headers."),
                () -> assertTrue(actualStatement.contains("DEPOSIT"), "Statement should include deposit transactions."),
                () -> assertTrue(actualStatement.contains("WITHDRAWAL"), "Statement should include withdrawal transactions."),
                () -> assertTrue(actualStatement.contains("100,00"), "Statement should correctly format deposit amounts."),
                () -> assertTrue(actualStatement.contains("69,25"), "Statement should correctly format the balance after withdrawal."),
                () -> assertTrue(actualStatement.contains("End of statement."), "Statement should have a proper ending.")
        );
    }



    @Test
    void testGenerateStatement_NoTransactions() {
        // Given: the above account with no transactions

        // When: The statement is generated
        String actualStatement = transactionService.printStatement(account);

        // Then: It should return a message indicating no transactions
        assertEquals("No transactions available for this account.", actualStatement, "Statement should correctly indicate no transactions.");
    }

    @Test
    void testGenerateStatement_NullAccount_ShouldThrowInvalidAccountException() {
        // Given : A null account & When & Then: Expect an InvalidAccountException to be thrown
        Exception exception = assertThrows(InvalidAccountException.class, () -> transactionService.printStatement(null));
        assertEquals("Account cannot be null.", exception.getMessage(), "Exception message should indicate null account.");
    }
}
