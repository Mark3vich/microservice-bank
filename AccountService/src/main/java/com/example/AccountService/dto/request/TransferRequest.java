package com.example.AccountService.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на перевод средств между аккаунтами")
public class TransferRequest {
    @Schema(description = "ID аккаунта отправителя")
    private UUID fromAccountId;
    @Schema(description = "ID аккаунта получателя")
    private UUID toAccountId;
    @Schema(description = "Сумма перевода", example = "100.0")
    private BigDecimal amount;
} 