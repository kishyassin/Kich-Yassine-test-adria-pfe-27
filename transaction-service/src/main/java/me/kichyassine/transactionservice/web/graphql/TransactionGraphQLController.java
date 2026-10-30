package me.kichyassine.transactionservice.web.graphql;

import me.kichyassine.transactionservice.domain.Transaction;
import me.kichyassine.transactionservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
public class TransactionGraphQLController {

    @Autowired
    private TransactionService transactionService;

    @QueryMapping
    public List<Transaction> transactions() {
        return transactionService.getAllTransactions();
    }

    @QueryMapping
    public Transaction transactionById(@Argument UUID id) {
        return transactionService.getTransactionById(id);
    }

    @QueryMapping
    public List<Transaction> transactionsByAccount(@Argument UUID accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }

    @MutationMapping
    public Transaction createTransaction(@Argument TransactionInput transaction) {
        Transaction tx = Transaction.builder()
                .accountId(transaction.accountId())
                .amount(BigDecimal.valueOf(transaction.amount()))
                .currency(transaction.currency())
                .merchantCategory(transaction.merchantCategory())
                .country(transaction.country())
                .build();
        return transactionService.createTransaction(tx);
    }

    public record TransactionInput(UUID accountId, double amount, String currency, String merchantCategory, String country) {}
}
