package com.example.aluguel_ms.ciclista.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

class MeioDePagamentoCartaoTest {
    @Test
    void testFromMapCompleto() {
        Map<String, Object> map = new HashMap<>();
        map.put("nomeTitular", "Maria Souza");
        map.put("numero", "6543210987654321");
        map.put("validade", "2026-11"); // Corrige formato para YearMonth yyyy-MM
        map.put("cvv", "456");
        MeioDePagamento m = MeioDePagamento.fromMap(map);
        assertEquals("Maria Souza", m.getNomeTitular());
        assertEquals("6543210987654321", m.getNumero());
        assertEquals(java.time.YearMonth.of(2026, 11), m.getValidade());
        assertEquals("456", m.getCvv());
    }

    @Test
    void testFromMapCamposFaltando() {
        Map<String, Object> map = new HashMap<>();
        map.put("nomeTitular", "Maria Souza");
        // falta numero, validade, cvv
        MeioDePagamento m = MeioDePagamento.fromMap(map);
        assertNull(m.getNumero());
        assertNull(m.getValidade());
        assertNull(m.getCvv());
    }
}
