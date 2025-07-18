package com.example.AccountService.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String eventType;

    @Lob
    private String payload;

    private boolean published;

    private LocalDateTime createdAt;

    public void markAsPublished() {
        this.published = true;
    }
} 