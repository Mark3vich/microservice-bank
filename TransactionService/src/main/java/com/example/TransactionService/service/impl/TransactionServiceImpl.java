package com.example.TransactionService.service.impl;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;

import com.example.TransactionService.constant.Api;
import com.example.TransactionService.service.TransactionService;
import com.example.TransactionService.model.CurrencyInfo;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
class TransactionServiceImpl implements TransactionService {

    public List<CurrencyInfo> getExchangeRate() {
        try {
            Api api = new Api();
            String url = api.getApi();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().length > 0) {
                String xml = new String(response.getBody(), "windows-1251");
                List<CurrencyInfo> currencies = parseXml(xml);
                currencies.forEach(System.out::println);
                return currencies;
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<CurrencyInfo> parseXml(String xml) {
        List<CurrencyInfo> result = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Используем windows-1251 для корректной обработки русских символов
            try (ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes("windows-1251"))) {
                Document doc = builder.parse(bais);
                doc.getDocumentElement().normalize();

                NodeList valuteList = doc.getElementsByTagName("Valute");
                for (int i = 0; i < valuteList.getLength(); i++) {
                    Element valute = (Element) valuteList.item(i);
                    String charCode = valute.getElementsByTagName("CharCode").item(0).getTextContent();
                    String name = valute.getElementsByTagName("Name").item(0).getTextContent();
                    String value = valute.getElementsByTagName("Value").item(0).getTextContent();
                    result.add(new CurrencyInfo(charCode, name, value));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BigDecimal convertCurrency(String from, String to, BigDecimal amount) {
        List<CurrencyInfo> rates = getExchangeRate();
        if (from.equalsIgnoreCase(to)) {
            return amount;
        }
        // RUB (RUR) — базовая валюта, в XML её нет, value всех валют — это стоимость 1 единицы в рублях
        BigDecimal fromRate = null;
        BigDecimal toRate = null;
        for (CurrencyInfo info : rates) {
            if (info.getCharCode().equalsIgnoreCase(from)) {
                fromRate = new BigDecimal(info.getValue().replace(",", "."));
            }
            if (info.getCharCode().equalsIgnoreCase(to)) {
                toRate = new BigDecimal(info.getValue().replace(",", "."));
            }
        }
        // Если одна из валют RUB, то курс = 1
        if (from.equalsIgnoreCase("RUB") || from.equalsIgnoreCase("RUR")) {
            fromRate = BigDecimal.ONE;
        }
        if (to.equalsIgnoreCase("RUB") || to.equalsIgnoreCase("RUR")) {
            toRate = BigDecimal.ONE;
        }
        if (fromRate == null || toRate == null) {
            throw new IllegalArgumentException("Unknown currency code");
        }
        // amount * fromRate / toRate
        return amount.multiply(fromRate).divide(toRate, 6, BigDecimal.ROUND_HALF_UP);
    }
}