package me.kichyassine.accountservice.mcp;

import me.kichyassine.accountservice.domain.Account;
import me.kichyassine.accountservice.service.AccountService;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AccountMcpTools {
    @Autowired
    private AccountService accountService;

    @McpTool(description = "Get all accounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @McpTool(description = "Get account by ID")
    public Account getAccountById(@McpToolParam(description = "Account ID") String id) {
        return accountService.getAccountById(UUID.fromString(id));
    }

    @McpTool(description = "Add new bank account")
    public Account addAccount(@McpToolParam(description = "Bank account") Account account) {
        return accountService.addAccount(account);
    }

    @McpTool(description = "Debit account")
    public Account debitAccount(
            @McpToolParam(description = "Account ID") String id,
            @McpToolParam(description = "Amount") double amount,
            @McpToolParam(description = "Currency") String currency
    ) {
        return accountService.debit(UUID.fromString(id), amount, currency);
    }

    @McpTool(description = "Credit account")
    public Account creditAccount(
            @McpToolParam(description = "Account ID") String id,
            @McpToolParam(description = "Amount") double amount,
            @McpToolParam(description = "Currency") String currency
    ) {
        return accountService.credit(UUID.fromString(id), amount, currency);
    }

    @McpTool(description = "Transfer money between two accounts")
    public String transfer(
            @McpToolParam(description = "Source Account ID") String sourceId,
            @McpToolParam(description = "Destination Account ID") String destId,
            @McpToolParam(description = "Amount") double amount,
            @McpToolParam(description = "Currency") String currency
    ) {
        accountService.transfer(UUID.fromString(sourceId), UUID.fromString(destId), amount, currency);
        return "Transfer completed successfully";
    }
}
