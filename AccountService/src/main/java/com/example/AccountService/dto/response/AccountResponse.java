package com.example.AccountService.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.AccountService.enums.Currency;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Schema(description = "Информация об аккаунте")
public class AccountResponse {
    @Schema(description = "ID пользователя")
    private UUID id;
    @Schema(description = "Имя пользователя", example = "Иван")
    private String name;
    @Schema(description = "Валюта", example = "EUR")
    private Currency currency;
    @Schema(description = "Баланс пользователя", example = "100.0")
    private BigDecimal balance;
}
