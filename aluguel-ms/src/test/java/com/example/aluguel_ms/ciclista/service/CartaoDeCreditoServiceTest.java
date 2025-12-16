package com.example.aluguel_ms.ciclista.service;

import com.example.aluguel_ms.ciclista.model.MeioDePagamento;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

class CartaoDeCreditoServiceTest {
    private final WebClient webClient = Mockito.mock(WebClient.class);
    private final CartaoDeCreditoService service = new CartaoDeCreditoService(webClient);

    @Test
    void testValidarCartaoValido() {
        MeioDePagamento m = new MeioDePagamento();
        m.setNomeTitular("Joao");
        m.setNumero("1234567890123");
        m.setValidade(LocalDate.of(2030, 12, 31));
        m.setCvv("123");

        // Mock encadeado do WebClient
        WebClient.RequestBodyUriSpec requestBodyUriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpec = Mockito.mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        Mockito.when(webClient.post()).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.uri(Mockito.anyString())).thenReturn(requestBodySpec);
        Mockito.when(requestBodySpec.contentType(Mockito.any())).thenReturn(requestBodySpec);
        Mockito.when(requestBodySpec.bodyValue(Mockito.any())).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToMono(Boolean.class)).thenReturn(reactor.core.publisher.Mono.just(Boolean.TRUE));

        assertTrue(service.validarCartao(m));
    }

    @Test
    void testValidarCartaoInvalidoNumero() {
        MeioDePagamento m = new MeioDePagamento();
        m.setNomeTitular("Joao");
        m.setNumero("123"); // muito curto
        m.setValidade(LocalDate.of(2030, 12, 31));
        m.setCvv("123");
        assertFalse(service.validarCartao(m));
    }

    @Test
    void testValidarCartaoInvalidoCvv() {
        MeioDePagamento m = new MeioDePagamento();
        m.setNomeTitular("Joao");
        m.setNumero("1234567890123");
        m.setValidade(LocalDate.of(2030, 12, 31));
        m.setCvv("1"); // muito curto
        assertFalse(service.validarCartao(m));
    }

    @Test
    void testValidarCartaoNull() {
        assertFalse(service.validarCartao(null));
    }
}
