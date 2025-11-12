package com.example.aluguel_ms.funcionario.model;

import com.example.aluguel_ms.model.Funcionario;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FuncionarioModelTest {

    @Test
    void testGettersAndSetters() {
        Funcionario f = new Funcionario();
        f.setMatricula("123");
        f.setSenha("senha");
        f.setEmail("email@teste.com");
        f.setNome("João");
        f.setIdade(30);
        f.setFuncao("administrativo");
        f.setCpf("12345678900");

        assertEquals("123", f.getMatricula());
        assertEquals("senha", f.getSenha());
        assertEquals("email@teste.com", f.getEmail());
        assertEquals("João", f.getNome());
        assertEquals(30, f.getIdade());
        assertEquals("administrativo", f.getFuncao());
        assertEquals("12345678900", f.getCpf());
    }
}