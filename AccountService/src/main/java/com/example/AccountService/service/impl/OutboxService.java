package com.example.AccountService.service.impl;

import com.example.AccountService.model.OutboxEvent;
import com.example.AccountService.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxRepository outboxRepository;
    private final MessageBroker messageBroker;

    // Запускается каждую минуту
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void processOutbox() {
        List<OutboxEvent> events = outboxRepository.findByPublishedFalse();
        for (OutboxEvent event : events) {
            messageBroker.publish(event);
            event.markAsPublished();
            outboxRepository.save(event);
        }
    }
} 