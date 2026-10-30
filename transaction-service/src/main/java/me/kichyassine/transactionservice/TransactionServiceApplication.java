package me.kichyassine.transactionservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import me.kichyassine.transactionservice.domain.Transaction;
import me.kichyassine.transactionservice.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@OpenAPIDefinition(info = @Info(title = "Transaction Service API", version = "1.0", description = "Transaction Service API Documentation"))
public class TransactionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(TransactionRepository transactionRepository) {
        return args -> {
            UUID testAccountId1 = UUID.randomUUID();
            UUID testAccountId2 = UUID.randomUUID();

            transactionRepository.save(Transaction.builder()
                    .accountId(testAccountId1)
                    .amount(BigDecimal.valueOf(45.50))
                    .currency("EUR")
                    .merchantCategory("SUPERMARKET")
                    .country("MA")
                    .createdAt(Instant.now().minus(Duration.ofHours(3)))
                    .build());

            transactionRepository.save(Transaction.builder()
                    .accountId(testAccountId1)
                    .amount(BigDecimal.valueOf(120.00))
                    .currency("EUR")
                    .merchantCategory("RESTAURANT")
                    .country("FR")
                    .createdAt(Instant.now().minus(Duration.ofHours(24)))
                    .build());

            transactionRepository.save(Transaction.builder()
                    .accountId(testAccountId1)
                    .amount(BigDecimal.valueOf(350.00))
                    .currency("EUR")
                    .merchantCategory("ELECTRONICS")
                    .country("MA")
                    .createdAt(Instant.now().minus(Duration.ofDays(2)))
                    .build());

            transactionRepository.save(Transaction.builder()
                    .accountId(testAccountId2)
                    .amount(BigDecimal.valueOf(25.00))
                    .currency("EUR")
                    .merchantCategory("COFFEE_SHOP")
                    .country("MA")
                    .createdAt(Instant.now().minus(Duration.ofMinutes(45)))
                    .build());

            transactionRepository.save(Transaction.builder()
                    .accountId(testAccountId2)
                    .amount(BigDecimal.valueOf(850.00))
                    .currency("EUR")
                    .merchantCategory("TRAVEL_AIRLINE")
                    .country("US")
                    .createdAt(Instant.now().minus(Duration.ofDays(4)))
                    .build());

        };
    }
}
