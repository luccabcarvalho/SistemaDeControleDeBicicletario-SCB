package com.example.aluguel_ms.ciclista.service;

import com.example.aluguel_ms.ciclista.model.MeioDePagamento;
import org.springframework.stereotype.Service;

@Service
public class CartaoDeCreditoService {
    public boolean validarCartao(MeioDePagamento cartao) {
       // Validação simples do cartão
        if (cartao == null || cartao.getNumero() == null || cartao.getNumero().length() < 13 || cartao.getCvv() == null || cartao.getCvv().length() < 3) {
            return false;
        }
        
        return true; // sempre sucesso
    }
}