package me.kichyassine.transactionservice.web.rest;

import me.kichyassine.transactionservice.domain.Transaction;
import me.kichyassine.transactionservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class TransactionRestController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/transactions/{id}")
    public Transaction getTransaction(@PathVariable UUID id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/transactions/account/{accountId}")
    public List<Transaction> getTransactionsByAccount(@PathVariable UUID accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }

    @PostMapping("/transactions")
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }
}
