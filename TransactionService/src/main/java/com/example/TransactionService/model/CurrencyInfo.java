package com.example.TransactionService.model;

public class CurrencyInfo {
    private String charCode;
    private String name;
    private String value;

    public CurrencyInfo(String charCode, String name, String value) {
        this.charCode = charCode;
        this.name = name;
        this.value = value;
    }

    public String getCharCode() {
        return charCode;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CurrencyInfo{" +
                "charCode='" + charCode + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
} 