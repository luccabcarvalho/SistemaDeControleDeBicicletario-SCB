package com.example.aluguel_ms.aluguel.service;

import com.example.aluguel_ms.aluguel.model.Aluguel;
import com.example.aluguel_ms.aluguel.model.Devolucao;
import com.example.aluguel_ms.aluguel.repository.AluguelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DevolucaoServiceTest {
    @Mock
    private AluguelRepository aluguelRepository;
    @InjectMocks
    private AluguelService aluguelService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testDevolverBicicletaSucessoSemValorExtra() {
        Aluguel aluguel = new Aluguel();
        aluguel.setId(1);
        aluguel.setBicicleta(10);
        aluguel.setHoraInicio(LocalDateTime.now().minusMinutes(100));
        aluguel.setHoraFim(null);
        when(aluguelRepository.findAll()).thenReturn(List.of(aluguel));
        when(aluguelRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        Optional<Devolucao> result = aluguelService.devolverBicicleta(2, 10);
        assertTrue(result.isPresent());
        Devolucao devolucao = result.get();
        assertEquals(10, devolucao.getIdBicicleta());
        assertEquals(2, devolucao.getIdTranca());
        assertEquals(0.0, devolucao.getValorExtra());
        assertEquals("ocupada", devolucao.getStatusTranca());
    }

    @Test
    void testDevolverBicicletaComValorExtra() {
        Aluguel aluguel = new Aluguel();
        aluguel.setId(2);
        aluguel.setBicicleta(20);
        aluguel.setHoraInicio(LocalDateTime.now().minusMinutes(200));
        aluguel.setHoraFim(null);
        when(aluguelRepository.findAll()).thenReturn(List.of(aluguel));
        when(aluguelRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        Optional<Devolucao> result = aluguelService.devolverBicicleta(3, 20);
        assertTrue(result.isPresent());
        Devolucao devolucao = result.get();
        assertEquals(20, devolucao.getIdBicicleta());
        assertEquals(3, devolucao.getIdTranca());
        assertTrue(devolucao.getValorExtra() > 0);
        assertEquals("ocupada", devolucao.getStatusTranca());
    }

    @Test
    void testDevolverBicicletaInvalida() {
        when(aluguelRepository.findAll()).thenReturn(Collections.emptyList());
        Optional<Devolucao> result = aluguelService.devolverBicicleta(2, 99);
        assertFalse(result.isPresent());
    }
}
