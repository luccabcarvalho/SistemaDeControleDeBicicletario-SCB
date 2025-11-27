package com.example.aluguel_ms.aluguel.service;

import com.example.aluguel_ms.aluguel.model.Aluguel;
import com.example.aluguel_ms.aluguel.repository.AluguelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AluguelServiceTest {
    @Mock
    private AluguelRepository aluguelRepository;
    @InjectMocks
    private AluguelService aluguelService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testAlugarBicicletaSucesso() {
        when(aluguelRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        Optional<Aluguel> result = aluguelService.alugarBicicleta(1, 2);
        assertTrue(result.isPresent());
        Aluguel aluguel = result.get();
        assertEquals(1, aluguel.getCiclista());
        assertEquals(102, aluguel.getBicicleta());
        assertEquals(2, aluguel.getTrancaInicio());
        assertNotNull(aluguel.getHoraInicio());
        assertEquals(1, aluguel.getCobranca());
    }

    @Test
    void testAlugarBicicletaTrancaInvalida() {
        Optional<Aluguel> result = aluguelService.alugarBicicleta(1, -1);
        assertFalse(result.isPresent());
    }

    @Test
    void testAlugarBicicletaSemBicicletaNaTranca() {
        Optional<Aluguel> result = aluguelService.alugarBicicleta(1, null);
        assertFalse(result.isPresent());
    }

    @Test
    void testAlugarBicicletaCiclistaInvalido() {
        Optional<Aluguel> result = aluguelService.alugarBicicleta(null, 2);
        assertFalse(result.isPresent());
    }
}
