package com.example.AccountService.exception;

public class InsufficientFundsException extends BusinessException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
