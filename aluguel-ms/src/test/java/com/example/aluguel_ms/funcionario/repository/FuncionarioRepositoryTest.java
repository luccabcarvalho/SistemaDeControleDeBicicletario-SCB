package com.example.aluguel_ms.funcionario.repository;

import com.example.aluguel_ms.model.Funcionario;
import com.example.aluguel_ms.repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FuncionarioRepositoryTest {

    private FuncionarioRepository repository;

    @BeforeEach
    void setUp() {
        repository = new FuncionarioRepository();
    }

    @Test
    void testSaveAndFindAll() {
        Funcionario f = new Funcionario();
        f.setNome("Maria");
        f.setFuncao("administrativo");
        repository.save(f);
        List<Funcionario> all = repository.findAll();
        assertFalse(all.isEmpty());
        assertEquals("Maria", all.get(0).getNome());
    }

    @Test
    void testFindByMatricula() {
        Funcionario f = new Funcionario();
        f.setNome("Carlos");
        f.setFuncao("reparador");
        Funcionario saved = repository.save(f);
        Optional<Funcionario> found = repository.findByMatricula(saved.getMatricula());
        assertTrue(found.isPresent());
        assertEquals("Carlos", found.get().getNome());
    }

    @Test
    void testUpdate() {
        Funcionario f = new Funcionario();
        f.setNome("Ana");
        f.setFuncao("administrativo");
        Funcionario saved = repository.save(f);
        saved.setNome("Ana Paula");
        Funcionario updated = repository.update(saved);
        assertNotNull(updated);
        assertEquals("Ana Paula", updated.getNome());
    }

    @Test
    void testDeleteByMatricula() {
        Funcionario f = new Funcionario();
        f.setNome("Pedro");
        f.setFuncao("reparador");
        Funcionario saved = repository.save(f);
        boolean deleted = repository.deleteByMatricula(saved.getMatricula());
        assertTrue(deleted);
        assertFalse(repository.findByMatricula(saved.getMatricula()).isPresent());
    }
}