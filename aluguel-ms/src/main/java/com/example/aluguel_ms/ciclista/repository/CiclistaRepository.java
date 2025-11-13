package com.example.aluguel_ms.ciclista.repository;

import com.example.aluguel_ms.ciclista.model.Ciclista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CiclistaRepository extends JpaRepository<Ciclista, Long> {
    Optional<Ciclista> findByEmail(String email);
}