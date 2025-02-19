package com.jawhar.bank_account.service;

import com.jawhar.bank_account.exception.InsufficientBalanceException;
import com.jawhar.bank_account.exception.InvalidAmountException;
import com.jawhar.bank_account.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setup() {
        // Given: An account with an initial balance
        account = new Account(12345L, new BigDecimal("100"), List.of());
    }

    @Test
    void testDeposit_ValidAmount() {
        // Given: A valid deposit amount
        BigDecimal depositAmount = new BigDecimal("500.75");

        // When: The deposit is made
        Account updatedAccount = accountService.deposit(account, depositAmount);

        // Then: The balance and transactions should be updated correctly
        assertAll(
                () -> assertEquals(new BigDecimal("600.75"), updatedAccount.balance(), "Balance should be updated correctly."),
                () -> assertEquals(1, updatedAccount.transactions().size(), "Transaction count should increase."),
                () -> assertEquals(new BigDecimal("600.75"), updatedAccount.transactions().get(0).postTransactionBalance(), "Transaction should store correct post-balance.")
        );
    }

    @Test
    void testWithdraw_ValidAmount() {
        // Given: A valid withdrawal amount
        BigDecimal withdrawAmount = new BigDecimal("50.50");

        // When: The withdrawal is made
        Account updatedAccount = accountService.withdraw(account, withdrawAmount);

        // Then: The balance and transactions should be updated correctly
        assertAll(
                () -> assertEquals(new BigDecimal("49.50"), updatedAccount.balance(), "Balance should be updated correctly."),
                () -> assertEquals(1, updatedAccount.transactions().size(), "Transaction count should increase."),
                () -> assertEquals(new BigDecimal("49.50"), updatedAccount.transactions().get(0).postTransactionBalance(), "Transaction should store correct post-balance.")
        );
    }

    @Test
    void testWithdraw_InsufficientBalance_ShouldThrowException() {
        // Given: A withdrawal amount greater than the balance
        BigDecimal withdrawAmount = new BigDecimal("600.00");

        // When & Then: The transaction should fail with an exception
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> accountService.withdraw(account, withdrawAmount));
        assertEquals("Withdrawal of 600.00 failed: insufficient balance. Current balance: 100", exception.getMessage());
    }

    @Test
    void testDeposit_NullAmount_ShouldThrowInvalidAmountException() {
        // Given: A null deposit amount

        // When & Then: The deposit should fail with an exception
        assertThrows(InvalidAmountException.class, () -> accountService.deposit(account, null));
    }

    @Test
    void testDeposit_NegativeAmount_ShouldThrowInvalidAmountException() {
        // Given: A negative deposit amount
        BigDecimal negativeAmount = new BigDecimal("-100");

        // When & Then: The deposit should fail with an exception
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> accountService.deposit(account, negativeAmount));
        assertEquals("DEPOSIT amount must be positive: -100", exception.getMessage());
    }

    @Test
    void testWithdraw_NegativeAmount_ShouldThrowInvalidAmountException() {
        // Given: A negative withdrawal amount
        BigDecimal negativeAmount = new BigDecimal("-50");

        // When & Then: The withdrawal should fail with an exception
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> accountService.withdraw(account, negativeAmount));
        assertEquals("WITHDRAWAL amount must be positive: -50", exception.getMessage());
    }
}
