package com.example.AccountService.interfaces;

import java.math.BigDecimal;

import com.example.AccountService.model.Account;

public interface TransferStep {
    void withdraw(Account from, BigDecimal amount);
    void deposit(Account to, BigDecimal amount);
    void compensate(Account from, Account to, BigDecimal amount);
}
