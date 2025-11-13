package com.example.aluguel_ms.ciclista.controller;

import com.example.aluguel_ms.ciclista.service.CartaoDeCreditoService;
import com.example.aluguel_ms.ciclista.service.CiclistaService;
import com.example.aluguel_ms.ciclista.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CiclistaController.class)
class CiclistaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CiclistaService ciclistaService;

    @MockBean
    private EmailService emailService;
    @MockBean
    private CartaoDeCreditoService cartaoService;


    @Test
    void testAtivarCiclista_QuandoSucesso() throws Exception {
        doNothing().when(ciclistaService).ativarCiclista(1L);

        mockMvc.perform(post("/ciclista/1/ativar"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cadastro completo com sucesso!"));
    }

    @Test
    void testAtivarCiclista_QuandoServiceLancaExcecao() throws Exception {
        doThrow(new RuntimeException("Dados inválidos."))
                .when(ciclistaService).ativarCiclista(2L);

        mockMvc.perform(post("/ciclista/2/ativar"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Dados inválidos."));
    }
}
