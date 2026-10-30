package me.kichyassine.transactionservice.kafka;

import lombok.extern.slf4j.Slf4j;
import me.kichyassine.transactionservice.domain.event.TransactionCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionEventConsumer {

    @KafkaListener(topics = "bank.transactions.created", groupId = "transaction-audit-group")
    public void consumeTransactionEvent(TransactionCreatedEvent event) {
        log.info("📢 [KAFKA AUDIT CONSUMER] Événement de transaction reçu avec succès (sans altération) :");
        log.info("   -> Event ID       : {}", event.getEventId());
        log.info("   -> Type           : {}", event.getEventType());
        log.info("   -> Transaction ID : {}", event.getTransactionId());
        log.info("   -> Account ID     : {}", event.getAccountId());
        log.info("   -> Montant        : {} {}", event.getAmount(), event.getCurrency());
        log.info("   -> Horodatage     : {}", event.getOccurredAt());
    }
}
