package com.example.aluguel_ms.aluguel.controller;

import com.example.aluguel_ms.aluguel.model.Aluguel;
import com.example.aluguel_ms.aluguel.service.AluguelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AluguelControllerTest {
    @Mock
    private AluguelService aluguelService;
    @InjectMocks
    private AluguelController aluguelController;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testAlugarBicicletaSucesso() {
        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(1);
        aluguel.setBicicleta(102);
        aluguel.setTrancaInicio(2);
        aluguel.setHoraInicio(java.time.LocalDateTime.now());
        aluguel.setCobranca(1);
        when(aluguelService.alugarBicicleta(1, 2)).thenReturn(Optional.of(aluguel));
        Map<String, Object> payload = new HashMap<>();
        payload.put("ciclista", 1);
        payload.put("trancaInicio", 2);
        ResponseEntity<?> response = aluguelController.alugarBicicleta(payload);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(aluguel, response.getBody());
    }

    @Test
    void testAlugarBicicletaDadosInvalidos() {
        when(aluguelService.alugarBicicleta(any(), any())).thenReturn(Optional.empty());
        Map<String, Object> payload = new HashMap<>();
        payload.put("ciclista", 1);
        payload.put("trancaInicio", -1);
        ResponseEntity<?> response = aluguelController.alugarBicicleta(payload);
        assertEquals(422, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);
        Map<?,?> body = (Map<?,?>) response.getBody();
        assertEquals("Dados Inválidos", body.get("erro"));
    }

    @Test
    void testAlugarBicicletaPayloadIncompleto() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("ciclista", 1);
        ResponseEntity<?> response = aluguelController.alugarBicicleta(payload);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);
        Map<?,?> body = (Map<?,?>) response.getBody();
        assertEquals("Requisição malformada: ciclista e trancaInicio são obrigatórios", body.get("erro"));
    }
}
