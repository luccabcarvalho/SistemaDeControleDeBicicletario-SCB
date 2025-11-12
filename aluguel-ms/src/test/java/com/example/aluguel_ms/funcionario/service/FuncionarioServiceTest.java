package com.example.aluguel_ms.funcionario.service;

import com.example.aluguel_ms.model.Funcionario;
import com.example.aluguel_ms.repository.FuncionarioRepository;
import com.example.aluguel_ms.service.FuncionarioService;
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
        when(repository.update(f)).thenReturn(f);
        Funcionario result = service.atualizarFuncionario(f);
        assertEquals(f, result);
        verify(repository).update(f);
    }

    @Test
    void testRemoverPorMatricula() {
        when(repository.deleteByMatricula("xyz")).thenReturn(true);
        boolean result = service.removerPorMatricula("xyz");
        assertTrue(result);
        verify(repository).deleteByMatricula("xyz");
    }
}