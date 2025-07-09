package com.example.TransactionService.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TransactionService.model.CurrencyInfo;
import com.example.TransactionService.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping("api/v1/transfer")
@Tag(name = "Transfer", description = "Эндпоинты для перевода денежных средств")
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(
        summary = "Курс валют",
        description = "Узнать информацию об валютах",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Информация получена",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = CurrencyInfo.class)))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Внутренняя ошибка сервера"
            )
        },
        tags = {"Transfer"}
    )
    
    @GetMapping("/currency")
    public ResponseEntity<List<CurrencyInfo>> authenticate() {
        List<CurrencyInfo> currencies = transactionService.getExchangeRate();
        return ResponseEntity.ok(currencies);
    }
}
