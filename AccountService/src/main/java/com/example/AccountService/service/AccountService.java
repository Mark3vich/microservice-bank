package com.example.AccountService.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.AccountService.dto.request.AccountRequest;
import com.example.AccountService.model.Account;

@Service
public interface AccountService {
    Account getAccount(UUID id);
    boolean createAccount(AccountRequest request);
    boolean updateAccount(AccountRequest request, UUID id);
    boolean deleteAccount(UUID id);
}
