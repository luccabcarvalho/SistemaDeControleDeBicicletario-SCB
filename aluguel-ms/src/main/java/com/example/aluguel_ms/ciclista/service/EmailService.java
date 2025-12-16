package com.example.aluguel_ms.ciclista.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

import java.util.Map;

@Service
public class EmailService {

    private final WebClient webClient;

    @Value("${externo.url}")
    private String externoUrl;

    public EmailService(WebClient webClient) {
        this.webClient = webClient;
    }

    public boolean enviarEmail(String destinatario, String assunto, String mensagem) {
        Map<String, Object> payload = Map.of(
                "destinatario", destinatario,
                "assunto", assunto,
                "mensagem", mensagem
        );
        try {
            webClient.post()
                    .uri(externoUrl + "/enviarEmail")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}