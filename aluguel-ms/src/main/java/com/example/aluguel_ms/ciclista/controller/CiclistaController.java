package com.example.aluguel_ms.ciclista.controller;

import com.example.aluguel_ms.ciclista.model.Ciclista;
import com.example.aluguel_ms.ciclista.model.MeioDePagamento;
import com.example.aluguel_ms.ciclista.service.CiclistaService;
import com.example.aluguel_ms.ciclista.service.EmailService;
import com.example.aluguel_ms.ciclista.service.CartaoDeCreditoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ciclista")
public class CiclistaController {

    private final CiclistaService service;
    private final EmailService emailService;
    private final CartaoDeCreditoService cartaoService;

    public CiclistaController(CiclistaService service, EmailService emailService, CartaoDeCreditoService cartaoService) {
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

        boolean cartaoValido = cartaoService.validarCartao(meioDePagamento);
        if (!cartaoValido) {
            return ResponseEntity.unprocessableEntity().body("Cartão inválido");
        }

        Ciclista criado = service.cadastrarCiclista(ciclista, meioDePagamento);
        emailService.enviarEmail(ciclista.getEmail(), "Bem-vindo ao sistema!");

        return ResponseEntity.status(201).body(criado);
    }

    @GetMapping
    public ResponseEntity<List<Ciclista>> listarCiclistas() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{idCiclista}")
    public ResponseEntity<?> buscarCiclista(@PathVariable Long idCiclista) {
        return service.buscarPorId(idCiclista)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("Ciclista não encontrado"));
    }

    @PutMapping("/{idCiclista}")
    public ResponseEntity<?> editarCiclista(@PathVariable Long idCiclista, @RequestBody Map<String, Object> ciclistaMap) {
        Ciclista dadosAtualizados = Ciclista.fromMap(ciclistaMap);
        Ciclista atualizado = service.atualizarCiclista(idCiclista, dadosAtualizados);
        if (atualizado != null) {
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.status(404).body("Ciclista não encontrado");
    }

    @DeleteMapping("/{idCiclista}")
    public ResponseEntity<?> removerCiclista(@PathVariable Long idCiclista) {
        boolean removido = service.removerPorId(idCiclista);
        if (removido) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404).body("Ciclista não encontrado");
    }

    @PostMapping("/{idCiclista}/ativar")
    public ResponseEntity<String> ativarCiclista(@PathVariable Long idCiclista) {
        try {
            service.ativarCiclista(idCiclista);

            return ResponseEntity.ok("Cadastro completo com sucesso!");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
