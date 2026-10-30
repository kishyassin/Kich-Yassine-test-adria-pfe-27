package me.kichyassine.ebankchatbot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EBankAiAgent {

    private final ChatClient chatClient;

    public EBankAiAgent(ChatClient.Builder chatClientBuilder,
                        ChatMemory chatMemory,
                        @Autowired(required = false) ToolCallbackProvider toolCallbackProvider) {
        var builder = chatClientBuilder
                .defaultSystem("""
                        Vous êtes un assistant bancaire intelligent pour la banque ADRIA.
                        Vous assistez les utilisateurs dans la gestion de leurs opérations bancaires :
                        - Consultation des comptes bancaires, soldes et devises
                        - Consultation et historique des transactions
                        - Enregistrement de nouvelles transactions
                        - Opérations bancaires : débit, crédit et virement d'un compte à un autre
                        Vous disposez d'outils (Tools MCP) connectés aux micro-services Account-Service et Transaction-Service pour exécuter ces actions.
                        Si une demande utilisateur n'a aucun rapport avec le domaine bancaire, répondez poliment que vous êtes un assistant dédié exclusivement aux opérations bancaires.
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build());

        if (toolCallbackProvider != null) {
            builder.defaultTools(toolCallbackProvider);
        }

        this.chatClient = builder.build();
    }

    public String chat(String message, String conversationId) {
        String convId = (conversationId != null && !conversationId.isBlank()) ? conversationId : "default";
        return chatClient.prompt(message)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, convId))
                .call()
                .content();
    }
}
