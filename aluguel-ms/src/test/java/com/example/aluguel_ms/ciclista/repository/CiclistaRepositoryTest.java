package com.example.aluguel_ms.ciclista.repository;

import com.example.aluguel_ms.ciclista.model.Ciclista;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CiclistaRepositoryTest {

    @Autowired
    private CiclistaRepository repository;

    @Test
    void testSaveAndFindAll() {
        Ciclista c = new Ciclista();
    c.setNome("Ana");
    c.setEmail("ana@teste.com");
    c.setCpf("12345678900");
    c.setNacionalidade("Brasileira");
    c.setNascimento(java.time.LocalDate.of(2000, 1, 1));
    c.setSenha("senha123");
    c.setUrlFotoDocumento("http://teste.com/doc1.png");
        repository.save(c);
        List<Ciclista> all = repository.findAll();
        assertFalse(all.isEmpty());
        assertEquals("Ana", all.get(0).getNome());
    }

    @Test
    void testFindById() {
        Ciclista c = new Ciclista();
    c.setNome("Bruno");
    c.setEmail("bruno@teste.com");
    c.setCpf("98765432100");
    c.setNacionalidade("Brasileira");
    c.setNascimento(java.time.LocalDate.of(1995, 5, 15));
    c.setSenha("senha123");
    c.setUrlFotoDocumento("http://teste.com/doc2.png");
        Ciclista saved = repository.save(c);
        Optional<Ciclista> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Bruno", found.get().getNome());
    }

    @Test
    void testUpdate() {
        Ciclista c = new Ciclista();
    c.setNome("Paula");
    c.setEmail("paula@teste.com");
    c.setCpf("11122233344");
    c.setNacionalidade("Brasileira");
    c.setNascimento(java.time.LocalDate.of(1998, 8, 20));
    c.setSenha("senha123");
    c.setUrlFotoDocumento("http://teste.com/doc3.png");
        Ciclista saved = repository.save(c);
        saved.setNome("Paula Souza");
        Ciclista updated = repository.save(saved);
        assertNotNull(updated);
        assertEquals("Paula Souza", updated.getNome());
    }

    @Test
    void testDeleteById() {
        Ciclista c = new Ciclista();
    c.setNome("Lucas");
    c.setEmail("lucas@teste.com");
    c.setCpf("55566677788");
    c.setNacionalidade("Brasileira");
    c.setNascimento(java.time.LocalDate.of(1987, 12, 5));
    c.setSenha("senha123");
    c.setUrlFotoDocumento("http://teste.com/doc4.png");
        Ciclista saved = repository.save(c);
        Optional<Ciclista> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        repository.delete(found.get());
        assertFalse(repository.findById(saved.getId()).isPresent());
    }
}