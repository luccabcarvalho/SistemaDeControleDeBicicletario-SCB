package com.example.aluguel_ms.funcionario.service;

import com.example.aluguel_ms.funcionario.model.Funcionario;
import com.example.aluguel_ms.funcionario.repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository repository;

    @InjectMocks
    private FuncionarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarFuncionario() {
        Funcionario f = new Funcionario();
        when(repository.save(f)).thenReturn(f);
        Funcionario result = service.criarFuncionario(f);
        assertEquals(f, result);
        verify(repository).save(f);
    }

    @Test
    void testListarTodos() {
        List<Funcionario> list = Arrays.asList(new Funcionario(), new Funcionario());
        when(repository.findAll()).thenReturn(list);
        List<Funcionario> result = service.listarTodos();
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void testBuscarPorMatricula() {
        Funcionario f = new Funcionario();
        when(repository.findByMatricula("abc")).thenReturn(Optional.of(f));
        Optional<Funcionario> result = service.buscarPorMatricula("abc");
        assertTrue(result.isPresent());
        verify(repository).findByMatricula("abc");
    }

    @Test
    void testAtualizarFuncionario() {
        Funcionario f = new Funcionario();
        when(repository.save(f)).thenReturn(f);
        Funcionario result = service.atualizarFuncionario(f);
        assertEquals(f, result);
        verify(repository).save(f);
    }

    @Test
    void testRemoverPorMatriculaSuccess() {
        Funcionario f = new Funcionario();
        when(repository.findByMatricula("xyz")).thenReturn(Optional.of(f));
        doNothing().when(repository).delete(f);
        boolean result = service.removerPorMatricula("xyz");
        assertTrue(result);
        verify(repository).findByMatricula("xyz");
        verify(repository).delete(f);
    }

    @Test
    void testRemoverPorMatriculaNotFound() {
        when(repository.findByMatricula("notfound")).thenReturn(Optional.empty());
        boolean result = service.removerPorMatricula("notfound");
        assertFalse(result);
        verify(repository).findByMatricula("notfound");
        verify(repository, never()).delete(any());
    }
}