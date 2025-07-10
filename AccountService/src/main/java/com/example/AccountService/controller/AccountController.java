package com.example.AccountService.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AccountService.dto.request.AccountRequest;
import com.example.AccountService.dto.request.TransferRequest;
import com.example.AccountService.model.Account;
import com.example.AccountService.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Tag(name = "Account Info", description = "API для взаимодействия с аккаунтом")
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = "Получить информацию об аккаунте", description = "Получение аккаунта по id", responses = {
                        @ApiResponse(responseCode = "200", description = "Успешно получен аккаунт", content = @Content),
                        @ApiResponse(responseCode = "400", description = "Аккаунт не найден", content = @Content)
    })
    @GetMapping("/getAccount/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @Operation(summary="Создать аккаунт", description = "Создание нового аккаунта", responses = {
        @ApiResponse(responseCode = "200", description = "Аккаунт успешно создан", content = @Content),
        @ApiResponse(responseCode = "400", description = "Некорректные данные", content = @Content)
    })
    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccount(@RequestBody AccountRequest account) {
        if(accountService.createAccount(account)) {
            return ResponseEntity.ok("Аккаунт успешно создан");
        }
        return ResponseEntity.ok("Аккаунт не создан");
    }

    @Operation(summary="Обновить аккаунт", description = "Обновить информацию аккаунта", responses = {
        @ApiResponse(responseCode = "200", description = "Аккаунт успешно создан", content = @Content),
        @ApiResponse(responseCode = "400", description = "Некорректные данные", content = @Content)
    })
    @PutMapping("/updateAccount/{id}")
    public ResponseEntity<String> updateAccount(@RequestBody AccountRequest account, @PathVariable UUID id) {
        if(accountService.updateAccount(account, id)) {
            return ResponseEntity.ok("Аккаунт успешно обновлен");
        }
        return ResponseEntity.ok("Аккаунт не обновлен");
    }

    @Operation(summary="Удалить аккаунт", description = "Удалить информацию об аккаунте", responses = {
        @ApiResponse(responseCode = "200", description = "Аккаунт успешно удален", content = @Content),
        @ApiResponse(responseCode = "400", description = "Некорректные данные", content = @Content)
    })
    @DeleteMapping("/deleteAccount/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID id) {
        if(accountService.deleteAccount(id)) {
            return ResponseEntity.ok("Аккаунт успешно удален");
        }
        return ResponseEntity.ok("Аккаунт не удален");
    }

    @Operation(summary = "Перевести деньги", description = "Перевод средств между аккаунтами", responses = {
        @ApiResponse(responseCode = "200", description = "Перевод успешно выполнен", content = @Content),
        @ApiResponse(responseCode = "400", description = "Ошибка перевода", content = @Content)
    })
    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferRequest transferRequest) {
        if (accountService.transferMoney(transferRequest.getFromAccountId(), transferRequest.getToAccountId(), transferRequest.getAmount())) {
            return ResponseEntity.ok("Перевод успешно выполнен");
        }
        return ResponseEntity.badRequest().body("Ошибка перевода");
    }
}
