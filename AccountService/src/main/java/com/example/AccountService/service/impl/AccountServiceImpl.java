package com.example.AccountService.service.impl;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.AccountService.client.CurrencyClient;
import com.example.AccountService.dto.request.AccountRequest;
import com.example.AccountService.enums.Currency;
import com.example.AccountService.exception.InvalidCurrencyException;
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
    private final CurrencyClient currencyClient;

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
        // Проверка на валидность валюты
        boolean validCurrency = false;
        for (Currency c : Currency.values()) {
            if (c == request.getCurrency()) {
                validCurrency = true;
                break;
            }
        }
        if (!validCurrency) {
            throw new InvalidCurrencyException("Валюта указана неверно");
        }
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

    @Override
    public boolean transferMoney(UUID fromAccountId, UUID toAccountId, BigDecimal amount) {
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Нельзя перевести деньги на тот же аккаунт");
        }
        Account fromAccount = accountRepository.findById(fromAccountId).orElse(null);
        Account toAccount = accountRepository.findById(toAccountId).orElse(null);
        if (fromAccount == null || toAccount == null) {
            throw new RuntimeException("Один из аккаунтов не найден");
        }
        BigDecimal amountToDeposit = amount;
        // Если валюты разные, конвертируем сумму
        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            amountToDeposit = currencyClient.convert(
                fromAccount.getCurrency().name(),
                toAccount.getCurrency().name(),
                amount
            );
            if (amountToDeposit == null) {
                throw new RuntimeException("Ошибка конвертации валюты");
            }
        }
        fromAccount.withdraw(amount);
        toAccount.deposit(amountToDeposit);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        return true;
    }
    
}
