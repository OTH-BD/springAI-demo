package net.bd.springai.controllers;

import net.bd.springai.output.CinModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class MultiModalController {
    private ChatClient chatClient;
    @Value("classpath:/images/.........jpeg") // ajouter une photo dans le dossier images pour test et entrez  le chemin ici
    private Resource image;
    @Value("classpath:/images/.........jpeg") // ajouter une photo dans le dossier images pour test et entrez  le chemin ici
    private Resource image2;


    public  MultiModalController(ChatClient.Builder builder, ChatMemory memory) {

        this.chatClient = builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .build();
    }
    @GetMapping("/describe")
    public CinModel describeImage(){
       return chatClient.prompt()
               .system("Donne moi une description de l'image fournie")
               .user(u->u.text("Décrire cette image")
                       .media(MediaType.IMAGE_JPEG, image)
               ).call()
               .entity(CinModel.class);
    }

    @PostMapping(value="/askImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String askImage(@RequestParam(name = "file") MultipartFile file, String question ) throws IOException {
        byte[] bytes =file.getBytes();
        return chatClient.prompt()
                .system("Repond a la question de l'utilisateur a propos de l'image manuscrite fournie ")
                .user(u->u.text(question)
                        .media(MediaType.IMAGE_JPEG, new ByteArrayResource(bytes))
                ).call()
                .content();
    }

}
