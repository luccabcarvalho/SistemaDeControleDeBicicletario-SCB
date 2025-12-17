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

    // Testes de sucesso dependem de fixtures válidas
    // @Test
    // void deveCadastrarCiclistaComSucesso() throws Exception {
    //     String payload = "{" +
    //             "\"ciclista\": {\"nome\": \"Teste\", \"nascimento\": \"2000-01-01\", \"cpf\": \"12345678900\", \"nacionalidade\": \"BR\", \"email\": \"teste@teste.com\", \"urlFotoDocumento\": \"url\", \"senha\": \"123\"}," +
    //             "\"meioDePagamento\": {\"numero\": \"1234123412341\", \"nomeTitular\": \"Teste\", \"validade\": \"12/30\", \"cvv\": \"123\"}}";
    //     mockMvc.perform(post("/ciclista")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(payload))
    //             .andExpect(status().isOk());
    // }
}
