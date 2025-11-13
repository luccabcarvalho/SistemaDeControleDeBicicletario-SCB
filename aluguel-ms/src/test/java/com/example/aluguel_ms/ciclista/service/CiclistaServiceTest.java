package com.example.aluguel_ms.ciclista.service;

import com.example.aluguel_ms.ciclista.model.Ciclista;
import com.example.aluguel_ms.ciclista.repository.CiclistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CiclistaServiceTest {

    @Mock
    private CiclistaRepository repository;

    @InjectMocks
    private CiclistaService ciclistaService;

    private Ciclista ciclistaDeTeste;


    @BeforeEach
    void setUp() {
        // cria um ciclista "PENDENTE" para ser usado nos testes
        ciclistaDeTeste = new Ciclista();
        ciclistaDeTeste.setId(1L);
        ciclistaDeTeste.setStatus("PENDENTE");
    }

    @Test
    void testAtivarCiclista_ComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(ciclistaDeTeste));

        ciclistaService.ativarCiclista(1L);

        verify(repository, times(1)).save(ciclistaDeTeste);

        assertEquals("ATIVO", ciclistaDeTeste.getStatus());

        assertNotNull(ciclistaDeTeste.getDataConfirmacaoEmail());
    }

    @Test
    void testAtivarCiclista_QuandoCiclistaNaoEncontrado_LancaExcecao() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ciclistaService.ativarCiclista(2L);
        });

        assertEquals("Dados inválidos.", exception.getMessage());


        verify(repository, never()).save(any());
    }


    @Test
    void testAtivarCiclista_QuandoCiclistaJaAtivo_LancaExcecao() {
        ciclistaDeTeste.setStatus("ATIVO");

        when(repository.findById(1L)).thenReturn(Optional.of(ciclistaDeTeste));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ciclistaService.ativarCiclista(1L);
        });

        assertEquals("Este registro não está pendente de ativação.", exception.getMessage());

        verify(repository, never()).save(any());
    }
}
