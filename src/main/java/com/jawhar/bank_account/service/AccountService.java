package com.jawhar.bank_account.service;

import com.jawhar.bank_account.exception.InsufficientBalanceException;
import com.jawhar.bank_account.model.Account;
import com.jawhar.bank_account.model.Transaction;
import com.jawhar.bank_account.model.TransactionType;
import com.jawhar.bank_account.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AccountService {

    public Account processTransaction(Account account, BigDecimal amount, TransactionType type) {
        ValidationUtil.validateAccount(account);
        ValidationUtil.validateAmount(amount, type.toString());

        // Calculate the new balance
        BigDecimal newBalance = (type == TransactionType.DEPOSIT)
                ? account.balance().add(amount)
                : account.balance().subtract(amount);

        // Validate sufficient balance for withdrawal
        if (type == TransactionType.WITHDRAWAL && newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException(String.format(
                    "Withdrawal of %s failed: insufficient balance. Current balance: %s",
                    amount, account.balance()));
        }

        // Create a new transaction
        Transaction newTransaction = new Transaction(
                LocalDateTime.now(),
                type,
                type == TransactionType.WITHDRAWAL ? amount.negate() : amount,
                newBalance
        );

        // Create a new immutable list of transactions (add new one)
        List<Transaction> updatedTransactions = new ArrayList<>(account.transactions());
        updatedTransactions.add(newTransaction);

        // Return a new immutable Account record with updated values
        return new Account(account.id(), newBalance, updatedTransactions);
    }

    public Account deposit(Account account, BigDecimal amount) {
        Account updatedAccount = processTransaction(account, amount, TransactionType.DEPOSIT);
        log.info("Deposit of {} successful. New balance: {}", amount, updatedAccount.balance());
        return updatedAccount;
    }

    public Account withdraw(Account account, BigDecimal amount) {
        Account updatedAccount = processTransaction(account, amount, TransactionType.WITHDRAWAL);
        log.info("Withdrawal of {} successful. New balance: {}", amount, updatedAccount.balance());
        return updatedAccount;
    }
}
