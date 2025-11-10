package com.example.aluguel_ms.repository;

import com.example.aluguel_ms.model.Funcionario;
import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class FuncionarioRepository {
    private final Map<String, Funcionario> funcionarios = new HashMap<>();

    public Funcionario save(Funcionario funcionario) {
        if (funcionario.getMatricula() == null) {
            funcionario.setMatricula(UUID.randomUUID().toString());
        }
        funcionarios.put(funcionario.getMatricula(), funcionario);
        return funcionario;
    }

    public List<Funcionario> findAll() {
        return new ArrayList<>(funcionarios.values());
    }

    public Optional<Funcionario> findByMatricula(String matricula) {
        return Optional.ofNullable(funcionarios.get(matricula));
    }
}