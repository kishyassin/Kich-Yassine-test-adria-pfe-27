package me.kichyassine.transactionservice.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionCreatedEvent {
    private UUID eventId;
    private String eventType;
    private UUID transactionId;
    private UUID accountId;
    private BigDecimal amount;
    private String currency;
    private Instant occurredAt;
}
