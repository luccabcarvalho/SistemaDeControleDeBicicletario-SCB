package com.example.aluguel_ms.ciclista.service;

import com.example.aluguel_ms.ciclista.model.Ciclista;
import com.example.aluguel_ms.ciclista.model.MeioDePagamento;
import com.example.aluguel_ms.ciclista.repository.CiclistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CiclistaServiceCartaoTest {
    @Mock
    private CiclistaRepository repository;
    @InjectMocks
    private CiclistaService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMeioDePagamentoFound() {
        Ciclista c = new Ciclista();
        MeioDePagamento m = new MeioDePagamento();
        c.setMeioDePagamento(m);
        when(repository.findById(1)).thenReturn(Optional.of(c));
        MeioDePagamento result = service.getMeioDePagamento(1);
        assertEquals(m, result);
    }

    @Test
    void testGetMeioDePagamentoNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        MeioDePagamento result = service.getMeioDePagamento(99);
        assertNull(result);
    }

    @Test
    void testAtualizarMeioDePagamentoSucesso() {
        Ciclista c = new Ciclista();
        MeioDePagamento novo = new MeioDePagamento();
        novo.setNomeTitular("Novo");
        novo.setNumero("1234567890123");
        novo.setValidade(java.time.YearMonth.of(2030, 12));
        novo.setCvv("123");
        when(repository.findById(1)).thenReturn(Optional.of(c));
        when(repository.save(c)).thenReturn(c);
        boolean result = service.atualizarMeioDePagamento(1, novo);
        assertTrue(result);
        assertEquals(novo, c.getMeioDePagamento());
    }

    @Test
    void testAtualizarMeioDePagamentoNotFound() {
        MeioDePagamento novo = new MeioDePagamento();
        when(repository.findById(99)).thenReturn(Optional.empty());
        boolean result = service.atualizarMeioDePagamento(99, novo);
        assertFalse(result);
    }
}
