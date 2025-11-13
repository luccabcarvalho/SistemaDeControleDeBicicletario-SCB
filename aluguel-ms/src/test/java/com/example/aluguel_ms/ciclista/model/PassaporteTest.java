package com.example.aluguel_ms.ciclista.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

class PassaporteTest {
    @Test
    void testGettersAndSetters() {
        Passaporte p = new Passaporte();
        p.setNumero("A1234567");
        p.setValidade(LocalDate.of(2030, 12, 31));
        p.setPais("Brasil");
        assertEquals("A1234567", p.getNumero());
        assertEquals(LocalDate.of(2030, 12, 31), p.getValidade());
        assertEquals("Brasil", p.getPais());
    }

    @Test
    void testConstrutorPadrao() {
        Passaporte p = new Passaporte();
        assertNotNull(p);
    }

    @Test
    void testFromMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("numero", "B7654321");
        map.put("validade", "2025-06-15");
        map.put("pais", "Portugal");
        Passaporte p = Passaporte.fromMap(map);
        assertEquals("B7654321", p.getNumero());
        assertEquals(LocalDate.of(2025, 6, 15), p.getValidade());
        assertEquals("Portugal", p.getPais());
    }
}
