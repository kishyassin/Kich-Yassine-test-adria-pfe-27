package me.kichyassine.accountservice;

import me.kichyassine.accountservice.domain.Account;
import me.kichyassine.accountservice.domain.AccountStatus;
import me.kichyassine.accountservice.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(AccountRepository accountRepository) {
        return args -> {
            accountRepository.save(Account.builder()
                    .customerReference("CUST-1")
                    .balance(5000.0)
                    .currency("EUR")
                    .status(AccountStatus.ACTIVE)
                    .build());
            accountRepository.save(Account.builder()
                    .customerReference("CUST-2")
                    .balance(2000.0)
                    .currency("EUR")
                    .status(AccountStatus.ACTIVE)
                    .build());
            accountRepository.save(Account.builder()
                    .customerReference("CUST-3")
                    .balance(100.0)
                    .currency("USD")
                    .status(AccountStatus.BLOCKED)
                    .build());
        };
    }
}
