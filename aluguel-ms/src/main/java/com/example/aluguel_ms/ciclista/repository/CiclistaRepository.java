
package com.example.aluguel_ms.ciclista.repository;
import org.springframework.stereotype.Repository;

import com.example.aluguel_ms.ciclista.model.Ciclista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface CiclistaRepository extends JpaRepository<Ciclista, Integer> {
    Optional<Ciclista> findByEmail(String email);
}