package net.bd.springai.controllers;

import net.bd.springai.output.Movie;
import net.bd.springai.output.MovieList;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiAgentStructuredController {
    private ChatClient chatClient;

    public AiAgentStructuredController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();

    }

    @GetMapping("/askAgent")
    public MovieList askLLM(String query){
        String systemMessage = " Vous etes un specialiste dans le dommaine du cinema, Repond a la question de lutilisateur a ce propos ";
        return  chatClient.prompt()
                .system(systemMessage)
                .user(query)
                .call()
                .entity(MovieList.class);

    }
}
