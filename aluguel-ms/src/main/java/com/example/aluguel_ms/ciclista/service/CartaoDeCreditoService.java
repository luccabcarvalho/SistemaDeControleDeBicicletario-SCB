package com.example.aluguel_ms.ciclista.service;

import com.example.aluguel_ms.ciclista.model.MeioDePagamento;
import org.springframework.stereotype.Service;

@Service
public class CartaoDeCreditoServiceFake {
    public boolean validarCartao(MeioDePagamento meioDePagamento) {
        return true; // sempre sucesso
    }
}