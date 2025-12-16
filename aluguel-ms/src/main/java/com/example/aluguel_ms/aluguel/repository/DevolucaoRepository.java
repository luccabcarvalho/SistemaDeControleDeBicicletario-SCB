package com.example.aluguel_ms.aluguel.repository;

import com.example.aluguel_ms.aluguel.model.Devolucao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevolucaoRepository extends JpaRepository<Devolucao, Integer> {
    // Métodos customizados
}
