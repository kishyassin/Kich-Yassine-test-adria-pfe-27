package me.kichyassine.accountservice.web.graphql;

import me.kichyassine.accountservice.domain.Account;
import me.kichyassine.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class AccountGraphQLController {
    @Autowired
    private AccountService accountService;

    @QueryMapping
    public List<Account> accounts() {
        return accountService.getAllAccounts();
    }

    @QueryMapping
    public Account accountById(@Argument UUID id) {
        return accountService.getAccountById(id);
    }

    @MutationMapping
    public Account addAccount(@Argument Account account) {
        return accountService.addAccount(account);
    }

    @MutationMapping
    public Account debitAccount(@Argument UUID id, @Argument double amount, @Argument String currency) {
        return accountService.debit(id, amount, currency);
    }

    @MutationMapping
    public Account creditAccount(@Argument UUID id, @Argument double amount, @Argument String currency) {
        return accountService.credit(id, amount, currency);
    }
}
