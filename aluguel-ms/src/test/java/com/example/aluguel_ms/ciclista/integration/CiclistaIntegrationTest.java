package com.example.aluguel_ms.ciclista.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CiclistaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRetornar400AoCadastrarCiclistaComPayloadInvalido() throws Exception {
        mockMvc.perform(post("/ciclista")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404AoBuscarCiclistaInexistente() throws Exception {
        mockMvc.perform(get("/ciclista/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar404AoAtivarCiclistaInexistente() throws Exception {
        mockMvc.perform(post("/ciclista/99999/ativar"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar200AoVerificarEmailInexistente() throws Exception {
        mockMvc.perform(get("/ciclista/existeEmail/emailinexistente@teste.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
