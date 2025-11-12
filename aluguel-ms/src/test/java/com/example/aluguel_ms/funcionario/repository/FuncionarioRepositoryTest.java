package com.example.aluguel_ms.funcionario.repository;

import com.example.aluguel_ms.funcionario.model.Funcionario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FuncionarioRepositoryTest {

    @Autowired
    private FuncionarioRepository repository;

    @Test
    void testSaveAndFindAll() {
        Funcionario f = new Funcionario("123", "Maria", "maria@teste.com", "senha", "12345678900", "administrativo", 30);
        repository.save(f);
        List<Funcionario> all = repository.findAll();
        assertFalse(all.isEmpty());
        assertEquals("Maria", all.get(0).getNome());
    }

    @Test
    void testFindByMatricula() {
        Funcionario f = new Funcionario("456", "Carlos", "carlos@teste.com", "senha", "98765432100", "reparador", 28);
        repository.save(f);
        Optional<Funcionario> found = repository.findByMatricula("456");
        assertTrue(found.isPresent());
        assertEquals("Carlos", found.get().getNome());
    }

    @Test
    void testUpdate() {
        Funcionario f = new Funcionario("789", "Ana", "ana@teste.com", "senha", "11122233344", "administrativo", 35);
        repository.save(f);
        f.setNome("Ana Paula");
        Funcionario updated = repository.save(f);
        assertNotNull(updated);
        assertEquals("Ana Paula", updated.getNome());
    }

    @Test
    void testDeleteByMatricula() {
        Funcionario f = new Funcionario("321", "Pedro", "pedro@teste.com", "senha", "55566677788", "reparador", 40);
        repository.save(f);
        Optional<Funcionario> found = repository.findByMatricula("321");
        assertTrue(found.isPresent());
        repository.delete(found.get());
        assertFalse(repository.findByMatricula("321").isPresent());
    }
}