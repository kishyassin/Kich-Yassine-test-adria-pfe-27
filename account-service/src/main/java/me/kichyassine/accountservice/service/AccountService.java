package me.kichyassine.accountservice.service;

import me.kichyassine.accountservice.domain.Account;
import me.kichyassine.accountservice.domain.AccountStatus;
import me.kichyassine.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(UUID id) {
        return accountRepository.findById(id).orElse(null);
    }

    public List<Account> getAccountsByCustomer(String customerReference) {
        return accountRepository.findByCustomerReference(customerReference);
    }

    public Account addAccount(Account account) {
        if (account.getStatus() == null) {
            account.setStatus(AccountStatus.ACTIVE);
        }
        return accountRepository.save(account);
    }

    public Account debit(UUID id, double amount, String currency) {
        Account account = getAccountById(id);
        if (account == null) throw new RuntimeException("Account not found: " + id);
        if (account.getStatus() != AccountStatus.ACTIVE) throw new RuntimeException("Account is not ACTIVE");
        if (currency != null && !account.getCurrency().equalsIgnoreCase(currency)) throw new RuntimeException("Currency mismatch");
        if (account.getBalance() < amount) throw new RuntimeException("Insufficient balance");

        account.setBalance(account.getBalance() - amount);
        return accountRepository.save(account);
    }

    public Account credit(UUID id, double amount, String currency) {
        Account account = getAccountById(id);
        if (account == null) throw new RuntimeException("Account not found: " + id);
        if (account.getStatus() != AccountStatus.ACTIVE) throw new RuntimeException("Account is not ACTIVE");
        if (currency != null && !account.getCurrency().equalsIgnoreCase(currency)) throw new RuntimeException("Currency mismatch");

        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

    public void transfer(UUID sourceId, UUID destinationId, double amount, String currency) {
        debit(sourceId, amount, currency);
        credit(destinationId, amount, currency);
    }

    public Account updateStatus(UUID id, AccountStatus status) {
        Account account = getAccountById(id);
        account.setStatus(status);
        return accountRepository.save(account);
    }
}
