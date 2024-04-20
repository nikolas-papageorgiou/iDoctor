package com.iDoctor.iDoctor.services;

import com.iDoctor.iDoctor.EnvironmentAccess;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
public class ChatGPTService {

    @Autowired
    private EnvironmentAccess environmentAccess;

    private String apiKey;
    private Assistant assistant;

    @PostConstruct
    public void init() {
        this.apiKey = environmentAccess.getApiKey();
        initAssistant();
    }

    private void initAssistant() {
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(OpenAiChatModel.withApiKey(apiKey))
                .chatMemory(chatMemory)
                .build();
    }

    public void updateApiKey(String newApiKey) {
        this.apiKey = newApiKey;
        initAssistant();  // Reinitialize the assistant with the new API key
    }

    interface Assistant {
        @SystemMessage("Hello ChatGPT. I am kindly requesting you to help medical professionals. You will be asked questions by them to diagnose and treat medical conditions.")
        String chat(String message);
    }

    @MessageMapping("/message")
    @SendTo("/topic/response")
    public String sendQuery(String message) throws Exception {
        return assistant.chat(message);
    }

    @MessageMapping("/updateApiKey")
    public void handleApiKeyUpdate(String newApiKey) {
        updateApiKey(newApiKey);
    }
}
