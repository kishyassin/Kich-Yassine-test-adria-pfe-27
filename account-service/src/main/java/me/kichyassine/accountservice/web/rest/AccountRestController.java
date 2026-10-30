package me.kichyassine.accountservice.web.rest;

import me.kichyassine.accountservice.domain.Account;
import me.kichyassine.accountservice.domain.AccountStatus;
import me.kichyassine.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AccountRestController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable UUID id) {
        return accountService.getAccountById(id);
    }

    @GetMapping("/accounts/customer/{customerReference}")
    public List<Account> getAccountsByCustomer(@PathVariable String customerReference) {
        return accountService.getAccountsByCustomer(customerReference);
    }

    @PostMapping("/accounts")
    public Account addAccount(@RequestBody Account account) {
        return accountService.addAccount(account);
    }

    @PostMapping("/accounts/{id}/debit")
    public Account debit(@PathVariable UUID id, @RequestParam double amount, @RequestParam(required = false) String currency) {
        return accountService.debit(id, amount, currency);
    }

    @PostMapping("/accounts/{id}/credit")
    public Account credit(@PathVariable UUID id, @RequestParam double amount, @RequestParam(required = false) String currency) {
        return accountService.credit(id, amount, currency);
    }

    @PostMapping("/accounts/transfer")
    public void transfer(@RequestParam UUID sourceId, @RequestParam UUID destinationId, @RequestParam double amount, @RequestParam(required = false) String currency) {
        accountService.transfer(sourceId, destinationId, amount, currency);
    }

    @PutMapping("/accounts/{id}/status")
    public Account updateStatus(@PathVariable UUID id, @RequestParam AccountStatus status) {
        return accountService.updateStatus(id, status);
    }
}
