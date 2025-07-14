package net.bd.springai.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AiAgentController {
    private ChatClient chatClient;


    public  AiAgentController(ChatClient.Builder builder, ChatMemory memory) {

        this.chatClient = builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .build();
    }

    @GetMapping("/chat")
    public String askLLM(String query) {
        List<Message> examples = List.of(
                new UserMessage("6+5"),
                new AssistantMessage("Le résultat est : 11")

        );
        return  chatClient.prompt()
                .system("Répond toujors au majuscule")
                .messages(examples)
                .user(query)
                .call()
                .content();

    }
}
