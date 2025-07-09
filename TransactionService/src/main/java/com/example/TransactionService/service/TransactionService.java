package com.example.TransactionService.service;

import java.util.List;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.TransactionService.model.CurrencyInfo;

@Service
public interface TransactionService {
    List<CurrencyInfo> getExchangeRate();

    /**
     * Конвертировать сумму из одной валюты в другую
     * @param from код исходной валюты (например, "USD")
     * @param to код целевой валюты (например, "RUB")
     * @param amount сумма для конвертации
     * @return сконвертированная сумма
     */
    BigDecimal convertCurrency(String from, String to, BigDecimal amount);
}
