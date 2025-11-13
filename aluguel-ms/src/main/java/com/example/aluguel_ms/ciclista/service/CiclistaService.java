package com.example.aluguel_ms.ciclista.service;

import com.example.aluguel_ms.ciclista.model.Ciclista;
import com.example.aluguel_ms.ciclista.model.MeioDePagamento;
import com.example.aluguel_ms.ciclista.repository.CiclistaRepository;
import org.springframework.stereotype.Service;

@Service
public class CiclistaService {

    private final CiclistaRepository repository;

    public CiclistaService(CiclistaRepository repository) {
        this.repository = repository;
    }

    public boolean existeEmail(String email) {
        return repository.findByEmail(email).isPresent();
    }

    public Ciclista cadastrarCiclista(Ciclista ciclista, MeioDePagamento meioDePagamento) {
        ciclista.setMeioDePagamento(meioDePagamento);
        return repository.save(ciclista);
    }
}