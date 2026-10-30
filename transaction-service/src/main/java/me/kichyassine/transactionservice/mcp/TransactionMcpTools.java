package me.kichyassine.transactionservice.mcp;

import me.kichyassine.transactionservice.domain.Transaction;
import me.kichyassine.transactionservice.service.TransactionService;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class TransactionMcpTools {

    @Autowired
    private TransactionService transactionService;

    @McpTool(description = "Get all banking transactions")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @McpTool(description = "Get transaction details by ID")
    public Transaction getTransactionById(@McpToolParam(description = "Transaction ID") String id) {
        return transactionService.getTransactionById(UUID.fromString(id));
    }

    @McpTool(description = "Get transactions for a specific bank account")
    public List<Transaction> getTransactionsByAccountId(@McpToolParam(description = "Account ID") String accountId) {
        return transactionService.getTransactionsByAccountId(UUID.fromString(accountId));
    }

    @McpTool(description = "Record a new transaction for an account")
    public Transaction createTransaction(
            @McpToolParam(description = "Account UUID") String accountId,
            @McpToolParam(description = "Amount") double amount,
            @McpToolParam(description = "Currency") String currency,
            @McpToolParam(description = "Merchant category") String merchantCategory,
            @McpToolParam(description = "Country") String country
    ) {
        Transaction tx = Transaction.builder()
                .accountId(UUID.fromString(accountId))
                .amount(BigDecimal.valueOf(amount))
                .currency(currency)
                .merchantCategory(merchantCategory)
                .country(country)
                .build();
        return transactionService.createTransaction(tx);
    }
}
