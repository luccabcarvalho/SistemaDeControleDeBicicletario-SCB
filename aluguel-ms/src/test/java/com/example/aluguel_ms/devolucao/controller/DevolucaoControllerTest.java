package com.example.aluguel_ms.devolucao.controller;

import com.example.aluguel_ms.aluguel.model.Devolucao;
import com.example.aluguel_ms.aluguel.model.Erro;
import com.example.aluguel_ms.aluguel.service.DevolucaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DevolucaoControllerTest {
    @Mock
    private DevolucaoService devolucaoService;
    private DevolucaoController devolucaoController;

    @BeforeEach
    void setUp() { 
        MockitoAnnotations.openMocks(this);
        devolucaoController = new DevolucaoController(devolucaoService);
    }

    @Test
    void testDevolverBicicletaSucesso() {
        Devolucao devolucao = new Devolucao();
        devolucao.setIdBicicleta(1);
        devolucao.setIdTranca(2);
        devolucao.setValorExtra(10.0);
        when(devolucaoService.processarDevolucao(2, 1)).thenReturn(Optional.of(devolucao));
        Map<String, Object> payload = new HashMap<>();
        payload.put("trancaId", 2);
        payload.put("bicicletaId", 1);
        ResponseEntity<?> response = devolucaoController.devolverBicicleta(payload);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(devolucao, response.getBody());
    }

    @Test
    void testDevolverBicicletaDadosInvalidos() {
        when(devolucaoService.processarDevolucao(any(), any())).thenReturn(Optional.empty());
        Map<String, Object> payload = new HashMap<>();
        payload.put("trancaId", 2);
        payload.put("bicicletaId", -1);
        ResponseEntity<?> response = devolucaoController.devolverBicicleta(payload);
        assertEquals(422, response.getStatusCodeValue());
        assertEquals("Dados Inválidos", response.getBody());
    }

    @Test
    void testDevolverBicicletaPayloadIncompleto() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("trancaId", 2);
        ResponseEntity<?> response = devolucaoController.devolverBicicleta(payload);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Dados Inválidos", response.getBody());
    }
}
