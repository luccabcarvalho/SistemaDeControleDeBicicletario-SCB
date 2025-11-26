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
        map.put("validade", "2026-11-30");
        map.put("cvv", "456");
        MeioDePagamento m = MeioDePagamento.fromMap(map);
        assertEquals("Maria Souza", m.getNomeTitular());
        assertEquals("6543210987654321", m.getNumero());
        assertEquals(LocalDate.of(2026, 11, 30), m.getValidade());
        assertEquals("456", m.getCvv());
    }

    @Test
    void testFromMapCamposFaltando() {
        Map<String, Object> map = new HashMap<>();
        map.put("nomeTitular", "Maria Souza");
        // falta numero, validade, cvv
        assertThrows(Exception.class, () -> MeioDePagamento.fromMap(map));
    }
}
