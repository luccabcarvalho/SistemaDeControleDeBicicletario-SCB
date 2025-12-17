package com.example.aluguel_ms.ciclista.service;

import com.example.aluguel_ms.ciclista.model.Ciclista;
import com.example.aluguel_ms.ciclista.repository.CiclistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.web.reactive.function.client.WebClient;

class CiclistaServiceTest {
    @Mock
    private com.example.aluguel_ms.aluguel.repository.AluguelRepository aluguelRepository;
    @Test
    void testCiclistaSemAluguelEmAbertoTrue() {
        when(aluguelRepository.findAll()).thenReturn(java.util.Collections.emptyList());
        boolean result = service.ciclistaSemAluguelEmAberto(1);
        assertTrue(result);
    }

    @Test
    void testCiclistaSemAluguelEmAbertoFalse() {
        com.example.aluguel_ms.aluguel.model.Aluguel aluguel = new com.example.aluguel_ms.aluguel.model.Aluguel();
        aluguel.setCiclista(1);
        aluguel.setHoraFim(null);
        when(aluguelRepository.findAll()).thenReturn(java.util.List.of(aluguel));
        boolean result = service.ciclistaSemAluguelEmAberto(1);
        assertFalse(result);
    }

    @Test
    void testGetBicicletaAlugadaNull() {
        when(aluguelRepository.findAll()).thenReturn(java.util.Collections.emptyList());
        Object result = service.getBicicletaAlugada(1);
        assertNull(result);
    }

    @Mock
    private WebClient webClient;

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void testGetBicicletaAlugadaComAluguel() {
        // Mock do WebClient para simular resposta da API de equipamento
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Map<String, Object> bicicleta = new HashMap<>();
        bicicleta.put("id", 123);
        when(responseSpec.bodyToMono(eq(Map.class))).thenReturn(reactor.core.publisher.Mono.just(bicicleta));

        service = new CiclistaService(repository, aluguelRepository, webClient);

        Object result = service.getBicicletaAlugada(1);
        assertNotNull(result);
        assertTrue(result instanceof Map);
        assertEquals(123, ((Map<?,?>)result).get("id"));
    }

    @Mock
    private CiclistaRepository repository;

    @InjectMocks
    private CiclistaService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // aluguelRepository agora é injetado via construtor, não é necessário setAluguelRepository
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

        @Test
        void testAtivarCiclistaSucesso() {
            Ciclista c = new Ciclista();
            c.setId(10);
            c.setStatus("pendente");
            when(repository.findById(10)).thenReturn(Optional.of(c));
            when(repository.save(c)).thenReturn(c);
            Ciclista result = service.ativarCiclista(10);
            assertEquals("ativo", result.getStatus());
            verify(repository).findById(10);
            verify(repository).save(c);
        }

        @Test
        void testAtivarCiclistaJaAtivo() {
            Ciclista c = new Ciclista();
            c.setId(11);
            c.setStatus("ativo");
            when(repository.findById(11)).thenReturn(Optional.of(c));
            Ciclista result = service.ativarCiclista(11);
            assertEquals("ativo", result.getStatus());
            verify(repository).findById(11);
            verify(repository, never()).save(any());
        }

        @Test
        void testAtivarCiclistaNaoEncontrado() {
            when(repository.findById(99)).thenReturn(Optional.empty());
            Ciclista result = service.ativarCiclista(99);
            assertNull(result);
            verify(repository).findById(99);
        }
}