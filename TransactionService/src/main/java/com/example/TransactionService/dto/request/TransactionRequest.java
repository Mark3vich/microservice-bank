package com.example.TransactionService.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.TransactionService.enums.Currency;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TransactionRequest {
    private UUID senderId;
    private UUID recipientId;
    private Currency currency;
    private BigDecimal balance;
}
