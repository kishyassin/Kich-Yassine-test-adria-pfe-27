package me.kichyassine.transactionservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kichyassine.transactionservice.domain.model.Account;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID accountId;
    private BigDecimal amount;
    private String currency;
    private String merchantCategory;
    private String country;
    private Instant createdAt;

    @Transient
    private Account account;
}
