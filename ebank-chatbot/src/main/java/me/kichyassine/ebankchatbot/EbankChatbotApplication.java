package me.kichyassine.ebankchatbot;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "E-Bank Agentic Chatbot API", version = "1.0", description = "Assistant bancaire conversationnel utilisant Spring AI et des connecteurs MCP"))
public class EbankChatbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankChatbotApplication.class, args);
    }

    @Bean
    @ConditionalOnMissingBean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder().build();
    }
}
