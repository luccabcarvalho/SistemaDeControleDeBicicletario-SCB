package com.example.aluguel_ms.cartao.controller;

import com.example.aluguel_ms.ciclista.model.MeioDePagamento;
import com.example.aluguel_ms.ciclista.service.CiclistaService;
import com.example.aluguel_ms.ciclista.service.EmailService;
import com.example.aluguel_ms.ciclista.service.CartaoDeCreditoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartaoDeCreditoControllerTest {
    @Mock
    private CiclistaService service;
    @Mock
    private EmailService emailService;
    @Mock
    private CartaoDeCreditoService cartaoService;
    private CartaoDeCreditoController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new CartaoDeCreditoController(service, emailService, cartaoService);
    }

    @Test
    void testGetCartaoDeCreditoFound() {
        MeioDePagamento meio = new MeioDePagamento();
        when(service.getMeioDePagamento(1)).thenReturn(meio);
        ResponseEntity<?> response = controller.getCartaoDeCredito(1);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(meio, response.getBody());
    }

    @Test
    void testGetCartaoDeCreditoNotFound() {
        when(service.getMeioDePagamento(99)).thenReturn(null);
        ResponseEntity<?> response = controller.getCartaoDeCredito(99);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Ciclista não encontrado", response.getBody());
    }

    @Test
    void testAlterarCartaoDeCreditoSucesso() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("nomeTitular", "Joao");
        payload.put("numero", "1234567890123");
        payload.put("validade", "2030-12");
        payload.put("cvv", "123");
        MeioDePagamento novoCartao = MeioDePagamento.fromMap(payload);
        when(cartaoService.validarCartao(any())).thenReturn(true);
        when(service.atualizarMeioDePagamento(eq(1), any())).thenReturn(true);
        when(emailService.enviarEmail(anyString(), anyString(), anyString())).thenReturn(true);
        ResponseEntity<?> response = controller.alterarCartaoDeCredito(1, payload);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testAlterarCartaoDeCreditoDadosInvalidos() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("nomeTitular", "Joao");
        ResponseEntity<?> response = controller.alterarCartaoDeCredito(1, payload);
        assertEquals(422, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof List, "O corpo da resposta deveria ser uma lista, mas foi: " + response.getBody());
        List<?> body = (List<?>) response.getBody();
        boolean contemMensagem = body.stream().anyMatch(msg -> msg != null && msg.toString().contains("Cartão recusado"));
        assertTrue(contemMensagem, "A lista de mensagens deveria conter 'Cartão recusado', mas foi: " + body);
    }

    @Test
    void testAlterarCartaoDeCreditoRecusado() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("nomeTitular", "Joao");
        payload.put("numero", "1234567890123");
        payload.put("validade", "2030-12"); 
        payload.put("cvv", "123");
        when(cartaoService.validarCartao(any())).thenReturn(false);
        ResponseEntity<?> response = controller.alterarCartaoDeCredito(1, payload);
        assertEquals(422, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof List);
        List<?> body = (List<?>) response.getBody();
        assertTrue(body.contains("Cartão recusado"));
    }

    @Test
    void testAlterarCartaoDeCreditoCiclistaNotFound() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("nomeTitular", "Joao");
        payload.put("numero", "1234567890123");
        payload.put("validade", "2030-12");
        payload.put("cvv", "123");
        when(cartaoService.validarCartao(any())).thenReturn(true);
        when(service.atualizarMeioDePagamento(eq(99), any())).thenReturn(false);
        ResponseEntity<?> response = controller.alterarCartaoDeCredito(99, payload);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Ciclista não encontrado", response.getBody());
    }

    @Test
    void testAlterarCartaoDeCreditoEmailErro() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("nomeTitular", "Joao");
        payload.put("numero", "1234567890123");
        payload.put("validade", "2030-12"); 
        payload.put("cvv", "123");
        when(cartaoService.validarCartao(any())).thenReturn(true);
        when(service.atualizarMeioDePagamento(eq(1), any())).thenReturn(true);
        when(emailService.enviarEmail(anyString(), anyString(), anyString())).thenReturn(false);
        ResponseEntity<?> response = controller.alterarCartaoDeCredito(1, payload);
        assertEquals(422, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Não foi possível enviar o email"));
    }
}
