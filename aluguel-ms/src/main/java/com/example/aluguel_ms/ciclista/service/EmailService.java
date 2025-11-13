package com.example.aluguel_ms.ciclista.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public boolean enviarEmail(String email, String mensagem) {
        return true; // sempre sucesso
    }
}