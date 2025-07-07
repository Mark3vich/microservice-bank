package com.example.AccountService.mapper;

public interface Mappable <E, D> {
    E toEntity(D dto);
    D toDto(E entity);
}
