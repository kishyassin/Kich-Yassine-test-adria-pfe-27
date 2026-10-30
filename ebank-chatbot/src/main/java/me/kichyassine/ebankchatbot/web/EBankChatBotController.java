package me.kichyassine.ebankchatbot.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kichyassine.ebankchatbot.agents.EBankAiAgent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Chatbot Agentique", description = "Endpoints d'interaction avec l'assistant bancaire intelligent")
public class EBankChatBotController {

    private final EBankAiAgent eBankAiAgent;

    public EBankChatBotController(EBankAiAgent eBankAiAgent) {
        this.eBankAiAgent = eBankAiAgent;
    }

    @GetMapping("/chat")
    @Operation(
            summary = "Interagir avec l'assistant bancaire IA",
            description = "Envoie une requête en langage naturel à l'agent conversationnel connecté aux outils MCP (consultation comptes/transactions, virements, dépôts)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Réponse formulée par l'agent IA après exécution éventuelle d'actions bancaires via MCP")
            }
    )
    public String chat(
            @Parameter(description = "Message ou ordre de l'utilisateur", example = "Donne-moi la liste de mes comptes", required = true)
            @RequestParam String message,
            @Parameter(description = "Identifiant de la session pour l'historique de conversation", example = "default")
            @RequestParam(defaultValue = "default") String conversationId
    ) {
        return eBankAiAgent.chat(message, conversationId);
    }
}
