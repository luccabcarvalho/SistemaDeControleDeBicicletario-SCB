package com.example.aluguel_ms.ciclista.controller;

import com.example.aluguel_ms.ciclista.model.Ciclista;
import com.example.aluguel_ms.ciclista.model.MeioDePagamento;
import com.example.aluguel_ms.ciclista.service.CiclistaService;
import com.example.aluguel_ms.ciclista.service.EmailServiceFake;
import com.example.aluguel_ms.ciclista.service.CartaoDeCreditoServiceFake;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ciclista")
public class ciclistaController {

    private final CiclistaService service;
    private final EmailServiceFake emailService;
    private final CartaoDeCreditoServiceFake cartaoService;

    public ciclistaController(CiclistaService service, EmailServiceFake emailService, CartaoDeCreditoServiceFake cartaoService) {
        this.service = service;
        this.emailService = emailService;
        this.cartaoService = cartaoService;
    }

    @GetMapping("/existeEmail/{email}")
    public ResponseEntity<Boolean> existeEmail(@PathVariable String email) {
        boolean existe = service.existeEmail(email);
        return ResponseEntity.ok(existe);
    }

    @PostMapping
    public ResponseEntity<?> cadastrarCiclista(@RequestBody Map<String, Object> payload) {
        Map<String, Object> ciclistaMap = (Map<String, Object>) payload.get("ciclista");
        Map<String, Object> meioDePagamentoMap = (Map<String, Object>) payload.get("meioDePagamento");

        Ciclista ciclista = Ciclista.fromMap(ciclistaMap);
        MeioDePagamento meioDePagamento = MeioDePagamento.fromMap(meioDePagamentoMap);

        // Validação do cartão
        boolean cartaoValido = cartaoService.validarCartao(meioDePagamento);
        if (!cartaoValido) {
            return ResponseEntity.unprocessableEntity().body("Cartão inválido");
        }

        Ciclista criado = service.cadastrarCiclista(ciclista, meioDePagamento);

        // Envio de email
        emailService.enviarEmail(ciclista.getEmail(), "Bem-vindo ao sistema!");

        return ResponseEntity.status(201).body(criado);
    }
}