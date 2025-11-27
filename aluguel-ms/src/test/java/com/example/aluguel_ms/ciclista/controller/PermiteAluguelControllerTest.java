package com.example.aluguel_ms.ciclista.controller;

import com.example.aluguel_ms.ciclista.model.Ciclista;
import com.example.aluguel_ms.ciclista.service.CiclistaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermiteAluguelControllerTest {
    @Mock
    private CiclistaService service;
    @InjectMocks
    private CiclistaController controller;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testPermiteAluguelCiclistaAtivo() {
        Ciclista c = new Ciclista();
        c.setId(1);
        c.setStatus("ativo");
        when(service.buscarPorId(1)).thenReturn(Optional.of(c));
        when(service.ciclistaSemAluguelEmAberto(1)).thenReturn(true);
        ResponseEntity<?> response = controller.permiteAluguel(1);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody());
    }

    @Test
    void testPermiteAluguelCiclistaNaoAtivo() {
        Ciclista c = new Ciclista();
        c.setId(2);
        c.setStatus("pendente");
        when(service.buscarPorId(2)).thenReturn(Optional.of(c));
        ResponseEntity<?> response = controller.permiteAluguel(2);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(false, response.getBody());
    }

    @Test
    void testPermiteAluguelCiclistaNaoEncontrado() {
        when(service.buscarPorId(99)).thenReturn(Optional.empty());
        ResponseEntity<?> response = controller.permiteAluguel(99);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Ciclista não encontrado", response.getBody());
    }
}
