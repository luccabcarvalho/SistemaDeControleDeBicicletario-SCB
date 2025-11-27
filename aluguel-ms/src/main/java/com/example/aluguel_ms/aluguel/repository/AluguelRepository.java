package com.example.aluguel_ms.aluguel.repository;

import com.example.aluguel_ms.aluguel.model.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AluguelRepository extends JpaRepository<Aluguel, Integer> {
    // Por enquanto, nenhum método customizado é necessário
}
