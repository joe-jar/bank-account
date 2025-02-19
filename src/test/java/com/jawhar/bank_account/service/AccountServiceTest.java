package com.jawhar.bank_account.service;

import com.jawhar.bank_account.exception.InsufficientBalanceException;
import com.jawhar.bank_account.exception.InvalidAmountException;
import com.jawhar.bank_account.model.Account;
import com.jawhar.bank_account.model.Transaction;
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
        account = new Account(12345L, new BigDecimal("100"), List.of()); // ✅ Use record constructor
    }

    @Test
    void testDeposit_ValidAmount() {
        BigDecimal depositAmount = new BigDecimal("500.75");
        Account updatedAccount = accountService.deposit(account, depositAmount);

        assertEquals(new BigDecimal("600.75"), updatedAccount.balance(), "Balance should be updated correctly.");
        assertEquals(1, updatedAccount.transactions().size(), "Transaction count should increase.");

        // ✅ Ensure the last transaction correctly records post-transaction balance
        Transaction lastTransaction = updatedAccount.transactions().get(0);
        assertEquals(new BigDecimal("600.75"), lastTransaction.postTransactionBalance(), "Transaction should store correct post-balance.");
    }

    @Test
    void testWithdraw_ValidAmount() {
        BigDecimal withdrawAmount = new BigDecimal("50.50");
        Account updatedAccount = accountService.withdraw(account, withdrawAmount);

        assertEquals(new BigDecimal("49.50"), updatedAccount.balance(), "Balance should be updated correctly.");
        assertEquals(1, updatedAccount.transactions().size(), "Transaction count should increase.");

        // ✅ Ensure the last transaction correctly records post-transaction balance
        Transaction lastTransaction = updatedAccount.transactions().get(0);
        assertEquals(new BigDecimal("49.50"), lastTransaction.postTransactionBalance(), "Transaction should store correct post-balance.");
    }

    @Test
    void testWithdraw_InsufficientBalance_ShouldThrowException() {
        BigDecimal withdrawAmount = new BigDecimal("600.00");
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class,
                () -> accountService.withdraw(account, withdrawAmount));
        assertEquals("Withdrawal of 600.00 failed: insufficient balance. Current balance: 100", exception.getMessage());
    }

    @Test
    void testDeposit_NullAmount_ShouldThrowInvalidAmountException() {
        assertThrows(InvalidAmountException.class, () -> accountService.deposit(account, null));
    }

    @Test
    void testDeposit_NegativeAmount_ShouldThrowInvalidAmountException() {
        BigDecimal negativeAmount = new BigDecimal("-100");
        InvalidAmountException exception = assertThrows(InvalidAmountException.class,
                () -> accountService.deposit(account, negativeAmount));
        assertEquals("DEPOSIT amount must be positive: -100", exception.getMessage());
    }

    @Test
    void testWithdraw_NegativeAmount_ShouldThrowInvalidAmountException() {
        BigDecimal negativeAmount = new BigDecimal("-50");
        InvalidAmountException exception = assertThrows(InvalidAmountException.class,
                () -> accountService.withdraw(account, negativeAmount));
        assertEquals("WITHDRAWAL amount must be positive: -50", exception.getMessage());
    }
}
