# Diagramme de Classes UML — ADRIA E-Bank

Ce document présente la modélisation UML des entités métier, des clients Feign avec tolérance aux pannes, des événements asynchrones Kafka et de l'agent IA.

## Schéma Mermaid du Diagramme de Classes

```mermaid
classDiagram
    direction TB

    class Account {
        -UUID id
        -String customerReference
        -AccountStatus status
        -String currency
        -double balance
        +debit(amount: double, currency: String): Account
        +credit(amount: double, currency: String): Account
        +transfer(destinationId: UUID, amount: double, currency: String): void
    }

    class AccountStatus {
        <<enumeration>>
        ACTIVE
        BLOCKED
    }

    class Transaction {
        -UUID id
        -UUID accountId
        -BigDecimal amount
        -String currency
        -String merchantCategory
        -String country
        -Instant createdAt
        +validateAmount(maxAmount: double): void
        +validateCurrency(accountCurrency: String): void
    }

    class AccountRestClient {
        <<interface / OpenFeign>>
        +getAccountById(id: UUID): AccountDto
        +debitAccount(id: UUID, amount: double, currency: String): AccountDto
        +getDefaultAccount(id: UUID, exception: Exception): AccountDto
    }

    class TransactionCreatedEvent {
        <<record / Kafka Event>>
        -UUID eventId
        -String eventType
        -UUID transactionId
        -UUID accountId
        -BigDecimal amount
        -String currency
        -Instant occurredAt
    }

    class TransactionEventConsumer {
        <<KafkaListener>>
        +onTransactionCreated(event: TransactionCreatedEvent): void
    }

    class EBankAiAgent {
        <<Spring AI Agent>>
        -ChatClient chatClient
        -ChatMemory chatMemory
        -ToolCallbackProvider toolCallbackProvider
        +chat(message: String, conversationId: String): String
    }

    Account --> AccountStatus : status
    Transaction ..> Account : accountId (Association Logique)
    AccountRestClient ..> Account : Interroge / Débite via REST
    Transaction ..> AccountRestClient : Vérifie solde & débite via Feign
    Transaction ..> TransactionCreatedEvent : Déclenche après persistance
    TransactionCreatedEvent ..> TransactionEventConsumer : bank.transactions.created
    EBankAiAgent ..> Account : Invoque MCP Tools
    EBankAiAgent ..> Transaction : Invoque MCP Tools
```

## Fichiers Images Générés

- **Image Vectorielle Haute Définition (4770x3270)** : [class_diagram.png](file:///Users/kishyassin/Desktop/cloned_github/test%20adria/diagrams/class_diagram.png)
- **Image Visuelle de Synthèse** : [class_diagram_visual.jpg](file:///Users/kishyassin/Desktop/cloned_github/test%20adria/diagrams/class_diagram_visual.jpg)
- **Script de Régénération** : `diagrams/generate_class_diagram.py`
