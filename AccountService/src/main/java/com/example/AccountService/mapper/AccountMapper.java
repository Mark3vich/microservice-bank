package com.example.AccountService.mapper;

import org.mapstruct.Mapper;

import com.example.AccountService.dto.response.AccountResponse;
import com.example.AccountService.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper extends Mappable<Account, AccountResponse> {
    @Override
    Account toEntity(AccountResponse dto);

    @Override
    AccountResponse toDto(Account entity);
}
