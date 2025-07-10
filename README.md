# Microservice Bank

## Описание

Microservice Bank — это учебный проект, реализующий банковскую систему на основе микросервисной архитектуры. Проект состоит из двух основных сервисов: AccountService и TransactionService. Каждый сервис — это отдельное Spring Boot приложение, взаимодействующее с собственной базой данных PostgreSQL.

## Структура проекта

- **AccountService** — сервис для управления банковскими счетами пользователей (создание, обновление, удаление, перевод средств).
- **TransactionService** — сервис для работы с валютами и конвертацией, а также получения информации о курсах валют.

## AccountService

### Основные возможности:
- Получение информации об аккаунте по ID
- Создание нового аккаунта
- Обновление информации об аккаунте
- Удаление аккаунта
- Перевод средств между аккаунтами

### Примеры эндпоинтов:
- `GET /api/v1/account/getAccount/{id}` — получить аккаунт по ID
- `POST /api/v1/account/createAccount` — создать аккаунт
- `PUT /api/v1/account/updateAccount/{id}` — обновить аккаунт
- `DELETE /api/v1/account/deleteAccount/{id}` — удалить аккаунт
- `POST /api/v1/account/transfer` — перевести средства между аккаунтами

### Подробно: Перевод средств между аккаунтами (`POST /api/v1/account/transfer`)

Этот метод позволяет перевести деньги с одного аккаунта на другой, включая автоматическую конвертацию валют при необходимости.

**Структура запроса:**
```json
{
  "fromAccountId": "<UUID отправителя>",
  "toAccountId": "<UUID получателя>",
  "amount": 100.0
}
```
- `fromAccountId` — UUID аккаунта, с которого списываются средства
- `toAccountId` — UUID аккаунта, на который зачисляются средства
- `amount` — сумма для перевода (BigDecimal)

**Бизнес-логика:**
1. Проверяется, что отправитель и получатель — разные аккаунты.
2. Проверяется существование обоих аккаунтов.
3. Если валюты аккаунтов различаются, сумма автоматически конвертируется через сервис TransactionService (`/api/v1/transfer/currency/convert`).
4. Со счета отправителя списывается сумма (если достаточно средств), на счет получателя зачисляется (с учетом конвертации).
5. Оба аккаунта сохраняются с обновленным балансом.

**Возможные ошибки:**
- Перевод на тот же аккаунт запрещен (`400 Bad Request`)
- Один из аккаунтов не найден (`400 Bad Request`)
- Недостаточно средств (`400 Bad Request`)
- Ошибка конвертации валюты (`400 Bad Request`)
- Неверная валюта или некорректные параметры (`400 Bad Request`)

**Пример успешного ответа:**
```
Перевод успешно выполнен
```
**Пример ошибки (недостаточно средств):**
```json
{
  "timestamp": "2024-07-09T12:34:56.789",
  "status": 400,
  "error": "Bad Request",
  "message": "Insufficient funds for withdrawal",
  "path": "/api/v1/account/transfer"
}
```

**Поддерживаемые валюты:**
AUD, AZN, GBP, AMD, BYN, BGN, BRL, HUF, VND, HKD, GEL, DKK, AED, USD, EUR, EGP, INR, IDR, KZT, CAD, QAR, KGS, CNY, MDL, NZD, NOK, PLN, RON, XDR, SGD, TJS, THB, TRY, TMT, UZS, UAH, CZK, SEK, CHF, RSD, ZAR, KRW, JPY

### Конфигурация (application.yaml):
- Порт: 8081
- БД: PostgreSQL, база `account_service`

## TransactionService

### Основные возможности:
- Получение информации о курсах валют
- Конвертация суммы из одной валюты в другую

### Примеры эндпоинтов:
- `GET /api/v1/transfer/currency` — получить список валют и их курсы
- `GET /api/v1/transfer/currency/convert?from=RUB&to=USD&amount=100` — конвертировать сумму

### Конфигурация (application.yaml):
- Порт: 8082
- БД: PostgreSQL, база `transaction_service`

## Технологии
- Java 21
- Spring Boot 3.5.3
- Spring Data JPA
- PostgreSQL
- MapStruct
- Lombok
- Liquibase (для миграций в TransactionService)
- OpenAPI/Swagger (документация API)

## Запуск проекта

1. Убедитесь, что у вас установлен Docker и PostgreSQL, либо настройте локальные базы `account_service` и `transaction_service`.
2. В каждой папке сервиса выполните:
   ```bash
   ./mvnw spring-boot:run
   ```
3. AccountService будет доступен на порту 8081, TransactionService — на 8082.

## Документация API

Swagger UI доступен по адресу:
- AccountService: http://localhost:8081/swagger-ui.html
- TransactionService: http://localhost:8082/swagger-ui.html

---

Проект предназначен для учебных целей и демонстрирует базовые принципы построения микросервисной архитектуры на Java. 