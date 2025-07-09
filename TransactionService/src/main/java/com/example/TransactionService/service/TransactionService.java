package com.example.TransactionService.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.TransactionService.dto.request.TransactionRequest;
import com.example.TransactionService.model.CurrencyInfo;

@Service
public interface TransactionService {
    boolean sendMoney(TransactionRequest transactionRequest);
    List<CurrencyInfo> getExchangeRate();
}
