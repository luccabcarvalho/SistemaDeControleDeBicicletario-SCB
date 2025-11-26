package com.example.aluguel_ms.ciclista.service;

import com.example.aluguel_ms.ciclista.model.MeioDePagamento;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class CartaoDeCreditoServiceTest {
    private final CartaoDeCreditoService service = new CartaoDeCreditoService();

    @Test
    void testValidarCartaoValido() {
        MeioDePagamento m = new MeioDePagamento();
        m.setNomeTitular("Joao");
        m.setNumero("1234567890123");
        m.setValidade(LocalDate.of(2030, 12, 31));
        m.setCvv("123");
        assertTrue(service.validarCartao(m));
    }

    @Test
    void testValidarCartaoInvalidoNumero() {
        MeioDePagamento m = new MeioDePagamento();
        m.setNomeTitular("Joao");
        m.setNumero("123"); // muito curto
        m.setValidade(LocalDate.of(2030, 12, 31));
        m.setCvv("123");
        assertFalse(service.validarCartao(m));
    }

    @Test
    void testValidarCartaoInvalidoCvv() {
        MeioDePagamento m = new MeioDePagamento();
        m.setNomeTitular("Joao");
        m.setNumero("1234567890123");
        m.setValidade(LocalDate.of(2030, 12, 31));
        m.setCvv("1"); // muito curto
        assertFalse(service.validarCartao(m));
    }

    @Test
    void testValidarCartaoNull() {
        assertFalse(service.validarCartao(null));
    }
}
