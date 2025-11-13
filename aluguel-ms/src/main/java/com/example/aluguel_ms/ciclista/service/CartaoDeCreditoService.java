package com.example.aluguel_ms.ciclista.service;

import com.example.aluguel_ms.ciclista.model.MeioDePagamento;
import org.springframework.stereotype.Service;

@Service
public class CartaoDeCreditoService {
    public boolean validarCartao() {
        return true; // sempre sucesso
    }
}