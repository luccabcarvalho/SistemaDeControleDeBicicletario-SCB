package com.example.aluguel_ms.devolucao.controller;

import com.example.aluguel_ms.aluguel.model.Devolucao;
import com.example.aluguel_ms.aluguel.model.Erro;
import com.example.aluguel_ms.aluguel.service.AluguelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DevolucaoControllerTest {
    @Mock
    private AluguelService aluguelService;
    private DevolucaoController devolucaoController;

    @BeforeEach
    void setUp() { 
        MockitoAnnotations.openMocks(this);
        devolucaoController = new DevolucaoController(aluguelService);
    }

    @Test
    void testDevolverBicicletaSucesso() {
        Devolucao devolucao = new Devolucao();
        devolucao.setIdBicicleta(1);
        devolucao.setIdTranca(2);
        devolucao.setValorExtra(10.0);
        when(aluguelService.devolverBicicleta(2, 1)).thenReturn(Optional.of(devolucao));
        Map<String, Object> payload = new HashMap<>();
        payload.put("idTranca", 2);
        payload.put("idBicicleta", 1);
        ResponseEntity<?> response = devolucaoController.devolverBicicleta(payload);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(devolucao, response.getBody());
    }

    @Test
    void testDevolverBicicletaDadosInvalidos() {
        when(aluguelService.devolverBicicleta(any(), any())).thenReturn(Optional.empty());
        Map<String, Object> payload = new HashMap<>();
        payload.put("idTranca", 2);
        payload.put("idBicicleta", -1);
        ResponseEntity<?> response = devolucaoController.devolverBicicleta(payload);
        assertEquals(422, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof List);
        List<?> erros = (List<?>) response.getBody();
        assertTrue(erros.get(0) instanceof Erro);
        assertEquals("Dados Inválidos", ((Erro) erros.get(0)).getMensagem());
    }

    @Test
    void testDevolverBicicletaPayloadIncompleto() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("idTranca", 2);
        ResponseEntity<?> response = devolucaoController.devolverBicicleta(payload);
        assertEquals(422, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof List);
        List<?> erros = (List<?>) response.getBody();
        assertTrue(erros.get(0) instanceof Erro);
        assertEquals("Dados Inválidos", ((Erro) erros.get(0)).getMensagem());
    }
}
