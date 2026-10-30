package me.kichyassine.ebankchatbot.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kichyassine.ebankchatbot.agents.EBankAiAgent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Chatbot", description = "Agent conversationnel bancaire")
public class EBankChatBotController {

    private final EBankAiAgent eBankAiAgent;

    public EBankChatBotController(EBankAiAgent eBankAiAgent) {
        this.eBankAiAgent = eBankAiAgent;
    }

    @GetMapping("/chat")
    @Operation(summary = "Interagir avec l'assistant bancaire IA", description = "Envoie une requête à l'agent conversationnel connecté aux outils MCP")
    public String chat(
            @RequestParam String message,
            @RequestParam(defaultValue = "default") String conversationId
    ) {
        return eBankAiAgent.chat(message, conversationId);
    }
}
