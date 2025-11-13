package com.example.aluguel_ms.ciclista.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CiclistaModelTest {

    @Test
    void testGettersAndSetters() {
        Ciclista c = new Ciclista();
        c.setId(1);
        c.setNome("Carlos");
        c.setEmail("carlos@teste.com");
        c.setCpf("12345678900");
        assertEquals(1, c.getId());
        assertEquals("Carlos", c.getNome());
        assertEquals("carlos@teste.com", c.getEmail());
        assertEquals("12345678900", c.getCpf());
    }
}