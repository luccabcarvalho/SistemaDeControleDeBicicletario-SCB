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
        c.setNacionalidade("Brasileiro");
        c.setNascimento(java.time.LocalDate.of(1990, 5, 20));
        c.setSenha("senha123");
        c.setUrlFotoDocumento("urlFoto");
        assertEquals(1, c.getId());
        assertEquals("Carlos", c.getNome());
        assertEquals("carlos@teste.com", c.getEmail());
        assertEquals("12345678900", c.getCpf());
        assertEquals("Brasileiro", c.getNacionalidade());
        assertEquals(java.time.LocalDate.of(1990, 5, 20), c.getNascimento());
        assertEquals("senha123", c.getSenha());
        assertEquals("urlFoto", c.getUrlFotoDocumento());
    }

    @Test
    void testConstrutorPadrao() {
        Ciclista c = new Ciclista();
        assertNotNull(c);
    }

    @Test
    void testFromMap() {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("nome", "Ana");
        map.put("nascimento", "2001-01-01");
        map.put("cpf", "98765432100");
        map.put("nacionalidade", "Portuguesa");
        map.put("email", "ana@teste.com");
        map.put("urlFotoDocumento", "fotoUrl");
        map.put("senha", "senhaAna");
        Ciclista c = Ciclista.fromMap(map);
        assertEquals("Ana", c.getNome());
        assertEquals(java.time.LocalDate.of(2001, 1, 1), c.getNascimento());
        assertEquals("98765432100", c.getCpf());
        assertEquals("Portuguesa", c.getNacionalidade());
        assertEquals("ana@teste.com", c.getEmail());
        assertEquals("fotoUrl", c.getUrlFotoDocumento());
        assertEquals("senhaAna", c.getSenha());
    }
}