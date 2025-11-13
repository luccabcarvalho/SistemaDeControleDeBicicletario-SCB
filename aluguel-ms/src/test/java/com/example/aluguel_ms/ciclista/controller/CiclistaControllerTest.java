package com.example.aluguel_ms.ciclista.controller;

import com.example.aluguel_ms.ciclista.model.Ciclista;
import com.example.aluguel_ms.ciclista.service.CiclistaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CiclistaControllerTest {

    @Mock
    private CiclistaService service;
    @Mock
    private com.example.aluguel_ms.ciclista.service.EmailService emailService;
    @Mock
    private com.example.aluguel_ms.ciclista.service.CartaoDeCreditoService cartaoService;

    private CiclistaController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new CiclistaController(service, emailService, cartaoService);
    }

    @Test
    void testListarCiclistas() {
        List<Ciclista> list = Arrays.asList(new Ciclista(), new Ciclista());
        when(service.listarTodos()).thenReturn(list);
        ResponseEntity<List<Ciclista>> response = controller.listarCiclistas();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testBuscarCiclistaFound() {
        Ciclista c = new Ciclista();
        when(service.buscarPorId(1)).thenReturn(Optional.of(c));
        ResponseEntity<Ciclista> response = controller.buscarCiclista(1);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(c, response.getBody());
    }

    @Test
    void testBuscarCiclistaNotFound() {
        when(service.buscarPorId(99)).thenReturn(Optional.empty());
        ResponseEntity<Ciclista> response = controller.buscarCiclista(99);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testEditarCiclistaSuccess() {
        Ciclista c = new Ciclista();
        c.setNome("Teste");
        c.setEmail("teste@teste.com");
        c.setCpf("12345678900");
        c.setNacionalidade("Brasileira");
        c.setNascimento(java.time.LocalDate.of(2000, 1, 1));
        c.setSenha("senha123");
        when(service.atualizarCiclista(eq(1), any())).thenReturn(c);
        Map<String, Object> ciclistaMap = new HashMap<>();
        ciclistaMap.put("nome", c.getNome());
        ciclistaMap.put("email", c.getEmail());
        ciclistaMap.put("cpf", c.getCpf());
        ciclistaMap.put("nacionalidade", c.getNacionalidade());
        ciclistaMap.put("nascimento", c.getNascimento().toString());
        ciclistaMap.put("senha", c.getSenha());
        ResponseEntity<?> response = controller.editarCiclista(1, ciclistaMap);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(c, response.getBody());
    }

    @Test
    void testEditarCiclistaNotFound() {
        when(service.atualizarCiclista(eq(99), any())).thenReturn(null);
        Map<String, Object> ciclistaMap = new HashMap<>();
        ciclistaMap.put("nome", "Teste");
        ciclistaMap.put("email", "teste@teste.com");
        ciclistaMap.put("cpf", "12345678900");
        ciclistaMap.put("nacionalidade", "Brasileira");
        ciclistaMap.put("nascimento", java.time.LocalDate.of(2000, 1, 1).toString());
        ciclistaMap.put("senha", "senha123");
        ResponseEntity<?> response = controller.editarCiclista(99, ciclistaMap);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testRemoverCiclistaSuccess() {
        when(service.removerPorId(1)).thenReturn(true);
        ResponseEntity<?> response = controller.removerCiclista(1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRemoverCiclistaNotFound() {
        when(service.removerPorId(99)).thenReturn(false);
        ResponseEntity<?> response = controller.removerCiclista(99);
        assertEquals(404, response.getStatusCodeValue());
    }
}