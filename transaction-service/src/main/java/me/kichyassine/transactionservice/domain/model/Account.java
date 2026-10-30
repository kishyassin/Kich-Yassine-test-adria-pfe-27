package me.kichyassine.transactionservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    private UUID id;
    private String customerReference;
    private String status;
    private String currency;
    private double balance;
}
