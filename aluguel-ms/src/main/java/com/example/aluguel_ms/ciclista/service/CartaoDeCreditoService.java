package com.example.aluguel_ms.ciclista.service;

import com.example.aluguel_ms.ciclista.model.MeioDePagamento;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartaoDeCreditoService {

    private final WebClient webClient;

    @Value("${externo.url}")
    private String externoUrl;

    public CartaoDeCreditoService(WebClient webClient) {
        this.webClient = webClient;
    }

    public boolean validarCartao(MeioDePagamento cartao) {
        if (cartao == null || cartao.getNumero() == null || cartao.getNumero().length() < 13 || cartao.getCvv() == null || cartao.getCvv().length() < 3) {
            return false;
        }

        // payload conforme esperado pelo endpoint externo
        Map<String, Object> payload = new HashMap<>();
        payload.put("numero", cartao.getNumero());
        payload.put("nome", cartao.getNomeTitular());
        payload.put("validade", cartao.getValidade());
        payload.put("cvv", cartao.getCvv());

        try {
            Boolean valido = webClient.post()
                    .uri(externoUrl + "/validaCartaoDeCredito")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            return Boolean.TRUE.equals(valido);
        } catch (Exception e) {
            return false;
        }
    }
}