package com.example.aluguel_ms.funcionario.controller;

import com.example.aluguel_ms.controller.FuncionarioController;
import com.example.aluguel_ms.model.Funcionario;
import com.example.aluguel_ms.service.FuncionarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FuncionarioControllerTest {

    @Mock
    private FuncionarioService service;

    @InjectMocks
    private FuncionarioController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarFuncionario() {
        Funcionario f = new Funcionario();
        when(service.criarFuncionario(f)).thenReturn(f);
        ResponseEntity<Funcionario> response = controller.criarFuncionario(f);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(f, response.getBody());
    }

    @Test
    void testListarFuncionarios() {
        List<Funcionario> list = Arrays.asList(new Funcionario(), new Funcionario());
        when(service.listarTodos()).thenReturn(list);
        ResponseEntity<List<Funcionario>> response = controller.listarFuncionarios();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testBuscarFuncionarioFound() {
        Funcionario f = new Funcionario();
        when(service.buscarPorMatricula("123")).thenReturn(Optional.of(f));
        ResponseEntity<Funcionario> response = controller.buscarFuncionario("123");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(f, response.getBody());
    }

    @Test
    void testBuscarFuncionarioNotFound() {
        when(service.buscarPorMatricula("999")).thenReturn(Optional.empty());
        ResponseEntity<Funcionario> response = controller.buscarFuncionario("999");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testEditarFuncionarioSuccess() {
        Funcionario f = new Funcionario();
        f.setNome("Teste");
        f.setEmail("teste@teste.com");
        f.setSenha("senha");
        f.setCpf("12345678900");
        f.setFuncao("administrativo");
        f.setIdade(25);
        when(service.buscarPorMatricula("123")).thenReturn(Optional.of(f));
        when(service.atualizarFuncionario(any())).thenReturn(f);
        ResponseEntity<?> response = controller.editarFuncionario("123", f);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(f, response.getBody());
    }

    @Test
    void testEditarFuncionarioNotFound() {
        Funcionario f = new Funcionario();
        when(service.buscarPorMatricula("999")).thenReturn(Optional.empty());
        ResponseEntity<?> response = controller.editarFuncionario("999", f);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testEditarFuncionarioValidationError() {
        Funcionario f = new Funcionario();
        f.setNome("");
        f.setEmail("");
        f.setSenha("");
        f.setCpf("");
        f.setFuncao("invalid");
        f.setIdade(0);
        when(service.buscarPorMatricula("123")).thenReturn(Optional.of(f));
        ResponseEntity<?> response = controller.editarFuncionario("123", f);
        assertEquals(422, response.getStatusCodeValue());
        List<?> erros = (List<?>) response.getBody();
        assertFalse(erros.isEmpty());
    }

    @Test
    void testRemoverFuncionarioSuccess() {
        when(service.removerPorMatricula("123")).thenReturn(true);
        ResponseEntity<?> response = controller.removerFuncionario("123");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRemoverFuncionarioNotFound() {
        when(service.removerPorMatricula("999")).thenReturn(false);
        ResponseEntity<?> response = controller.removerFuncionario("999");
        assertEquals(404, response.getStatusCodeValue());
    }
}