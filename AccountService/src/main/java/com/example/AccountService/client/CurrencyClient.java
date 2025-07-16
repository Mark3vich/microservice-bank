package com.example.AccountService.client;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "currency-service", url = "http://localhost:8082")
public interface CurrencyClient {
    @GetMapping("/api/v1/transfer/currency/convert")
    BigDecimal convert(
        @RequestParam("from") String from,
        @RequestParam("to") String to,
        @RequestParam("amount") BigDecimal amount
    );
}
