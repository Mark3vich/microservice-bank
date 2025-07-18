package com.example.AccountService.service.impl;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.AccountService.client.CurrencyClient;
import com.example.AccountService.dto.request.AccountRequest;
import com.example.AccountService.enums.Currency;
import com.example.AccountService.exception.InvalidCurrencyException;
import com.example.AccountService.model.Account;
import com.example.AccountService.model.OutboxEvent;
import com.example.AccountService.repository.AccountRepository;
import com.example.AccountService.repository.OutboxRepository;
import com.example.AccountService.service.AccountService;

import com.example.AccountService.interfaces.TransferStep;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final CurrencyClient currencyClient;
    private final SagaOrchestrator sagaOrchestrator;
    private final OutboxRepository outboxRepository;

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
    @Transactional
    public boolean transferMoney(UUID fromAccountId, UUID toAccountId, BigDecimal amount) {
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Нельзя перевести деньги на тот же аккаунт");
        }
        Account fromAccount = accountRepository.findById(fromAccountId).orElse(null);
        Account toAccount = accountRepository.findById(toAccountId).orElse(null);
        if (fromAccount == null || toAccount == null) {
            throw new RuntimeException("Один из аккаунтов не найден");
        }
        final BigDecimal amountToDeposit;
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
        } else {
            amountToDeposit = amount;
        }
        boolean result = sagaOrchestrator.transfer(fromAccountId, toAccountId, amount, new TransferStep() {
            @Override
            public void withdraw(Account from, BigDecimal amt) {
                from.withdraw(amt);
            }
            @Override
            public void deposit(Account to, BigDecimal amt) {
                to.deposit(amountToDeposit);
            }
            @Override
            public void compensate(Account from, Account to, BigDecimal amt) {
                from.deposit(amt);
                to.withdraw(amountToDeposit);
            }
        });
        if (result) {
            OutboxEvent event = OutboxEvent.builder()
                .eventType("MoneyTransferred")
                .payload("{\"fromAccountId\":\"" + fromAccountId + "\",\"toAccountId\":\"" + toAccountId + "\",\"amount\":" + amount + "}")
                .published(false)
                .createdAt(LocalDateTime.now())
                .build();
            outboxRepository.save(event);
        }
        return result;
    }
    
}
