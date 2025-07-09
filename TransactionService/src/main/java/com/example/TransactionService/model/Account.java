package com.example.TransactionService.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.TransactionService.enums.Currency;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private Currency currency;
    private BigDecimal balance;
}
