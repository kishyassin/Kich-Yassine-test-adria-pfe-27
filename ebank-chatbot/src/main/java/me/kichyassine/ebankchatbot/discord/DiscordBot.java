package me.kichyassine.ebankchatbot.discord;

import com.zgamelogic.discord.annotations.DiscordController;
import com.zgamelogic.discord.annotations.DiscordMapping;
import me.kichyassine.ebankchatbot.agents.EBankAiAgent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@DiscordController
@ConditionalOnProperty(name = "discord.enabled", havingValue = "true")
public class DiscordBot {

    private final EBankAiAgent ebankAiAgent;

    public DiscordBot(EBankAiAgent ebankAiAgent) {
        this.ebankAiAgent = ebankAiAgent;
    }

    @DiscordMapping
    private void perform(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String query = event.getMessage().getContentRaw();
        String response = ebankAiAgent.chat(query, event.getAuthor().getId());
        event.getChannel().sendMessage(response).queue();
    }
}
