package com.example.aluguel_ms.ciclista.service;

import com.example.aluguel_ms.ciclista.model.Ciclista;
import com.example.aluguel_ms.ciclista.repository.CiclistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CiclistaServiceTest {

    @Mock
    private CiclistaRepository repository;

    @InjectMocks
    private CiclistaService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarTodos() {
        List<Ciclista> list = Arrays.asList(new Ciclista(), new Ciclista());
        when(repository.findAll()).thenReturn(list);
        List<Ciclista> result = service.listarTodos();
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void testBuscarPorId() {
        Ciclista c = new Ciclista();
        when(repository.findById(1)).thenReturn(Optional.of(c));
        Optional<Ciclista> result = service.buscarPorId(1);
        assertTrue(result.isPresent());
        verify(repository).findById(1);
    }

    @Test
    void testAtualizarCiclista() {
        Ciclista c = new Ciclista();
        when(repository.findById(1)).thenReturn(Optional.of(c));
        when(repository.save(c)).thenReturn(c);
        Ciclista result = service.atualizarCiclista(1, c);
        assertEquals(c, result);
        verify(repository).findById(1);
        verify(repository).save(c);
    }

    @Test
    void testRemoverPorIdSuccess() {
        Ciclista c = new Ciclista();
        when(repository.findById(2)).thenReturn(Optional.of(c));
        doNothing().when(repository).deleteById(2);
        boolean result = service.removerPorId(2);
        assertTrue(result);
        verify(repository).findById(2);
        verify(repository).deleteById(2);
    }

    @Test
    void testRemoverPorIdNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        boolean result = service.removerPorId(99);
        assertFalse(result);
        verify(repository).findById(99);
        verify(repository, never()).deleteById(any());
    }
}