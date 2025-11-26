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

    private static final String CICLISTA_NAO_ENCONTRADO = "Ciclista não encontrado";

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

    @PostMapping("/{idCiclista}/ativar")
    public ResponseEntity<Object> ativarCiclista(@PathVariable Integer idCiclista) {
        Ciclista ciclista = service.buscarPorId(idCiclista).orElse(null);
        if (ciclista == null) {
            return ResponseEntity.status(404).body(CICLISTA_NAO_ENCONTRADO);
        }
        if ("ativo".equals(ciclista.getStatus())) {
            return ResponseEntity.unprocessableEntity().body("Ciclista já está ativo");
        }
        if ("pendente".equals(ciclista.getStatus())) {
            Ciclista ativado = service.ativarCiclista(idCiclista);
            return ResponseEntity.ok(ativado);
        }
        return ResponseEntity.unprocessableEntity().body("Não foi possível ativar o ciclista");
    }

    @PostMapping
    public ResponseEntity<String> cadastrarCiclista(@RequestBody Map<String, Object> payload) {
        Map<String, Object> ciclistaMap = (Map<String, Object>) payload.get("ciclista");
        Map<String, Object> meioDePagamentoMap = (Map<String, Object>) payload.get("meioDePagamento");

        Ciclista ciclista = Ciclista.fromMap(ciclistaMap);
        MeioDePagamento meioDePagamento = MeioDePagamento.fromMap(meioDePagamentoMap);

        boolean cartaoValido = cartaoService.validarCartao(meioDePagamento);
        if (!cartaoValido) {
            return ResponseEntity.unprocessableEntity().body("Cartão inválido");
        }

        Ciclista criado = service.cadastrarCiclista(ciclista, meioDePagamento);
        emailService.enviarEmail();

        return ResponseEntity.status(201).body(criado.toString());
    }

    @GetMapping
    public ResponseEntity<List<Ciclista>> listarCiclistas() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{idCiclista}")
    public ResponseEntity<Ciclista> buscarCiclista(@PathVariable Integer idCiclista) {
        return service.buscarPorId(idCiclista)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    @PutMapping("/{idCiclista}")
    public ResponseEntity<Ciclista> editarCiclista(@PathVariable Integer idCiclista, @RequestBody Map<String, Object> ciclistaMap) {
        Ciclista dadosAtualizados = Ciclista.fromMap(ciclistaMap);
        Ciclista atualizado = service.atualizarCiclista(idCiclista, dadosAtualizados);
        if (atualizado != null) {
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/{idCiclista}")
    public ResponseEntity<Void> removerCiclista(@PathVariable Integer idCiclista) {
        boolean removido = service.removerPorId(idCiclista);
        if (removido) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404).build();
    }

    @GetMapping("/cartaoDeCredito/{idCiclista}")
    public ResponseEntity<Object> getCartaoDeCredito(@PathVariable Integer idCiclista) {
        MeioDePagamento meio = service.getMeioDePagamento(idCiclista);
        if (meio == null) {
            return ResponseEntity.status(404).body(CICLISTA_NAO_ENCONTRADO);
        }
        return ResponseEntity.ok(meio);
    }

    @PutMapping("/cartaoDeCredito/{idCiclista}")
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