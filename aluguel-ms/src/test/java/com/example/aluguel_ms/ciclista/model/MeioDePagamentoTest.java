package com.example.aluguel_ms.ciclista.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

class MeioDePagamentoTest {
    @Test
    void testGettersAndSetters() {
        MeioDePagamento m = new MeioDePagamento();
        m.setId(1);
        m.setNomeTitular("João Silva");
        m.setNumero("1234567890123456");
        m.setValidade(LocalDate.of(2028, 5, 31));
        m.setCvv("123");
        assertEquals(1, m.getId());
        assertEquals("João Silva", m.getNomeTitular());
        assertEquals("1234567890123456", m.getNumero());
        assertEquals(LocalDate.of(2028, 5, 31), m.getValidade());
        assertEquals("123", m.getCvv());
    }

    @Test
    void testConstrutorPadrao() {
        MeioDePagamento m = new MeioDePagamento();
        assertNotNull(m);
    }

    @Test
    void testFromMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("nomeTitular", "Maria Souza");
        map.put("numero", "6543210987654321");
        map.put("validade", "2026-11-30");
        map.put("cvv", "456");
        MeioDePagamento m = MeioDePagamento.fromMap(map);
        assertEquals("Maria Souza", m.getNomeTitular());
        assertEquals("6543210987654321", m.getNumero());
        assertEquals(LocalDate.of(2026, 11, 30), m.getValidade());
        assertEquals("456", m.getCvv());
    }
}
