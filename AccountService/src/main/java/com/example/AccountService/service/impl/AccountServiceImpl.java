package com.example.AccountService.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.AccountService.dto.request.AccountRequest;
import com.example.AccountService.model.Account;
import com.example.AccountService.repository.AccountRepository;
import com.example.AccountService.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Account getAccount(UUID id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            log.error("Account not found");
            throw new RuntimeException("Account not found");
        }
        return account;
    }

    @Override
    public boolean createAccount(AccountRequest request) {
        Account account = new Account();

        account.setName(request.getName());
        account.setCurrency(request.getCurrency());
        account.setBalance(request.getBalance());

        accountRepository.save(account);

        return true;
    }

    @Override
    public boolean updateAccount(AccountRequest request, UUID id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            log.error("Account not found");
            throw new RuntimeException("Account not found");
        }
        account.setName(request.getName());
        account.setCurrency(request.getCurrency());
        account.setBalance(request.getBalance());

        accountRepository.save(account);

        return true;
    }

    @Override
    public boolean deleteAccount(UUID id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            log.error("Account not found");
            throw new RuntimeException("Account not found");
        }

        accountRepository.delete(account);

        return true;
    }
    
}
