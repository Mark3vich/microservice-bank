package com.example.AccountService.service.impl;

import com.example.AccountService.model.OutboxEvent;
import org.springframework.stereotype.Component;

@Component
public class MessageBroker {
    public void publish(OutboxEvent event) {
        // Здесь должна быть интеграция с Kafka, RabbitMQ и т.д.
        System.out.println("[MessageBroker] Published event: " + event.getEventType() + ", payload: " + event.getPayload());
    }
} 