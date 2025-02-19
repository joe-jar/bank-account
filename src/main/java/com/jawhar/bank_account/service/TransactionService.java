package com.jawhar.bank_account.service;

import com.jawhar.bank_account.exception.InvalidAccountException;
import com.jawhar.bank_account.model.Account;
import com.jawhar.bank_account.util.TransactionFormatterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionService {

    public String printStatement(Account account) {
        if (account == null) {
            log.warn("Attempted to generate a statement for a null account.");
            throw new InvalidAccountException("Account cannot be null.");
        }

        Long accountId = account.id();
        log.info("Generating bank statement for Account ID: {}", accountId);

        // Guard clause for empty transactions
        if (account.transactions().isEmpty()) {
            log.warn("Account ID {} has no transactions. Statement will be empty.", accountId);
            return "No transactions available for this account.";
        }

        String statement = TransactionFormatterUtil.generateStatement(account);

        log.info("Bank statement generated successfully for Account ID: {}", accountId);
        log.info("\n{}", statement);

        return statement;
    }
}
