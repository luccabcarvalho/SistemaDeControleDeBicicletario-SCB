package com.example.aluguel_ms.cartao.controller;

import com.example.aluguel_ms.ciclista.model.MeioDePagamento;
import com.example.aluguel_ms.ciclista.service.CiclistaService;
import com.example.aluguel_ms.ciclista.service.EmailService;
import com.example.aluguel_ms.ciclista.service.CartaoDeCreditoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cartaoDeCredito")
public class CartaoDeCreditoController {
    private final CiclistaService service;
    private final EmailService emailService;
    private final CartaoDeCreditoService cartaoService;
    private static final String CICLISTA_NAO_ENCONTRADO = "Ciclista não encontrado";

    public CartaoDeCreditoController(CiclistaService service, EmailService emailService, CartaoDeCreditoService cartaoService) {
        this.service = service;
        this.emailService = emailService;
        this.cartaoService = cartaoService;
    }

    @GetMapping("/{idCiclista}")
    public ResponseEntity<Object> getCartaoDeCredito(@PathVariable Integer idCiclista) {
        MeioDePagamento meio = service.getMeioDePagamento(idCiclista);
        if (meio == null) {
            return ResponseEntity.status(404).body(CICLISTA_NAO_ENCONTRADO);
        }
        return ResponseEntity.ok(meio);
    }

    @PutMapping("/{idCiclista}")
    public ResponseEntity<Object> alterarCartaoDeCredito(@PathVariable Integer idCiclista, @RequestBody Map<String, Object> payload) {
        MeioDePagamento novoCartao;
        try {
            novoCartao = MeioDePagamento.fromMap(payload);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(List.of("Dados inválidos"));
        }
        boolean cartaoValido = cartaoService.validarCartao(novoCartao);
        if (!cartaoValido) {
            return ResponseEntity.unprocessableEntity().body(List.of("Cartão recusado"));
        }
        boolean cartaoAtualizado = service.atualizarMeioDePagamento(idCiclista, novoCartao);
        if (!cartaoAtualizado) {
            return ResponseEntity.status(404).body(CICLISTA_NAO_ENCONTRADO);
        }
        boolean emailEnviado = emailService.enviarEmail();
        if (!emailEnviado) {
            return ResponseEntity.unprocessableEntity().body(List.of("Não foi possível enviar o email"));
        }
        return ResponseEntity.ok().build();
    }
}
