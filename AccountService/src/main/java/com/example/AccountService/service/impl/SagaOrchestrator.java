package com.example.AccountService.service.impl;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.AccountService.repository.AccountRepository;
import com.example.AccountService.interfaces.TransferStep;
import com.example.AccountService.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SagaOrchestrator {
    private final AccountRepository accountRepository;

    public boolean transfer(UUID fromAccountId, UUID toAccountId, BigDecimal amount, TransferStep step) {
        Account fromAccount = accountRepository.findById(fromAccountId).orElse(null);
        Account toAccount = accountRepository.findById(toAccountId).orElse(null);

        if (fromAccount == null || toAccount == null) {
            throw new RuntimeException("Account not found");
        }

        try {
            // Step 1: Withdraw
            step.withdraw(fromAccount, amount);
            accountRepository.save(fromAccount);

            // Step 2: Deposit
            step.deposit(toAccount, amount);
            accountRepository.save(toAccount);

            return true;
        } catch (Exception e) {
            // Compensating transaction
            step.compensate(fromAccount, toAccount, amount);
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            throw new RuntimeException("Saga failed, compensation applied: " + e.getMessage());
        }
    }
}
