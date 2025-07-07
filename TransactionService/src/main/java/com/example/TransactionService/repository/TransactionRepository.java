package com.example.TransactionService.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.TransactionService.model.Transaction;

public interface  TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("SELECT t FROM Transaction t WHERE t.id = :id")
    Transaction findTransactionById(UUID id);
}
