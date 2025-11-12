package com.example.aluguel_ms.funcionario.repository;

import com.example.aluguel_ms.funcionario.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {
    Optional<Funcionario> findByMatricula(String matricula);
}