package me.kichyassine.transactionservice.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import me.kichyassine.transactionservice.domain.model.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "account-service", url = "${account.service.url:http://localhost:8082}")
public interface AccountRestClient {

    @CircuitBreaker(name = "account-service", fallbackMethod = "getDefaultAccount")
    @GetMapping("/accounts/{id}")
    Account getAccountById(@PathVariable UUID id);

    @PostMapping("/accounts/{id}/debit")
    Account debitAccount(
            @PathVariable UUID id,
            @RequestParam double amount,
            @RequestParam String currency
    );

    default Account getDefaultAccount(UUID id, Exception exception) {
        Account fallback = new Account();
        fallback.setId(id);
        fallback.setStatus("UNKNOWN");
        fallback.setCustomerReference("Fallback-ServiceUnavailable");
        return fallback;
    }
}
