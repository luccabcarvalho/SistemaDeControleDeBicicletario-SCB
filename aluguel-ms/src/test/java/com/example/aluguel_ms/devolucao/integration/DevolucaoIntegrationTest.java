package com.example.aluguel_ms.devolucao.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DevolucaoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRetornar400QuandoPayloadInvalido() throws Exception {
        mockMvc.perform(post("/devolucao")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void deveRetornar400QuandoParametrosNaoSaoInteiros() throws Exception {
        String payload = "{" +
                "\"trancaId\": \"abc\"," +
                "\"bicicletaId\": \"xyz\"}";
        mockMvc.perform(post("/devolucao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void deveRetornar422QuandoDadosInvalidos() throws Exception {
        // IDs válidos mas que não existem ou não podem ser devolvidos
        String payload = "{" +
                "\"trancaId\": 9999," +
                "\"bicicletaId\": 8888}";
        mockMvc.perform(post("/devolucao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.erro").exists());
    }
}
