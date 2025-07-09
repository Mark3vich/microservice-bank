package com.example.TransactionService.constant;

import lombok.Getter;

@Getter
public class Api {
    private String api = "https://www.cbr-xml-daily.ru/daily.xml";
    private String account = "http://localhost:8081/api/v1/account/getAccount/";
}
