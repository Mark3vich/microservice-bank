package com.example.TransactionService.mapper;

import org.mapstruct.Mapper;

import com.example.TransactionService.dto.request.TransactionRequest;
import com.example.TransactionService.model.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper extends Mappable<Transaction, TransactionRequest> {
    @Override
    Transaction toEntity(TransactionRequest dto);

    @Override
    TransactionRequest toDto(Transaction entity);
}
