package me.kichyassine.transactionservice.service;

import me.kichyassine.transactionservice.domain.Transaction;
import me.kichyassine.transactionservice.domain.model.Account;
import me.kichyassine.transactionservice.feign.AccountRestClient;
import me.kichyassine.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRestClient accountRestClient;

    @Value("${bank.transaction.max-amount:1000}")
    private double maxAmount;

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction : transactions) {
            try {
                Account account = accountRestClient.getAccountById(transaction.getAccountId());
                transaction.setAccount(account);
            } catch (Exception ignored) {}
        }
        return transactions;
    }

    public Transaction getTransactionById(UUID id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction != null) {
            try {
                Account account = accountRestClient.getAccountById(transaction.getAccountId());
                transaction.setAccount(account);
            } catch (Exception ignored) {}
        }
        return transaction;
    }

    public List<Transaction> getTransactionsByAccountId(UUID accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public Transaction createTransaction(Transaction transaction) {
        // 1. Validation du montant strictement positif
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Le montant doit être strictement positif");
        }

        // 2. Respect du plafond configuré
        if (transaction.getAmount().doubleValue() > maxAmount) {
            throw new RuntimeException("Le montant dépasse le plafond autorisé de " + maxAmount);
        }

        // 3. Consultation du compte bancaire via OpenFeign
        Account account = accountRestClient.getAccountById(transaction.getAccountId());
        if (account == null || "UNKNOWN".equals(account.getStatus())) {
            throw new RuntimeException("Compte bancaire introuvable ou service indisponible");
        }

        // 4. Vérifier que le compte est actif
        if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {
            throw new RuntimeException("Le compte n'est pas actif (Statut: " + account.getStatus() + ")");
        }

        // 5. Vérifier la devise
        if (transaction.getCurrency() != null && !account.getCurrency().equalsIgnoreCase(transaction.getCurrency())) {
            throw new RuntimeException("La devise de la transaction ne correspond pas à celle du compte");
        }

        // 6. Vérifier le solde disponible
        if (account.getBalance() < transaction.getAmount().doubleValue()) {
            throw new RuntimeException("Solde insuffisant sur le compte");
        }

        // 7. Enregistrer la transaction
        if (transaction.getCreatedAt() == null) {
            transaction.setCreatedAt(Instant.now());
        }
        Transaction savedTransaction = transactionRepository.save(transaction);

        // 8. Mettre à jour le solde du compte (débit)
        accountRestClient.debitAccount(transaction.getAccountId(), transaction.getAmount().doubleValue(), transaction.getCurrency());

        savedTransaction.setAccount(account);
        return savedTransaction;
    }
}
