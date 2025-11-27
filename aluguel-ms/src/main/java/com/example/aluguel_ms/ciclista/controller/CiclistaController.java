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


    @GetMapping("/{idCiclista}/permiteAluguel")
    public ResponseEntity<Object> permiteAluguel(@PathVariable Integer idCiclista) {
        return service.buscarPorId(idCiclista)
                .map(ciclista -> {
                    boolean ativo = "ativo".equals(ciclista.getStatus());
                    boolean semAluguel = service.ciclistaSemAluguelEmAberto(idCiclista);
                    boolean podeAlugar = ativo && semAluguel;
                    return ResponseEntity.ok((Object)podeAlugar);
                })
                .orElseGet(() -> ResponseEntity.status(404).body((Object)CICLISTA_NAO_ENCONTRADO));
    }

    @GetMapping("/{idCiclista}/bicicletaAlugada")
    public ResponseEntity<Object> bicicletaAlugada(@PathVariable Integer idCiclista) {
        if (!service.buscarPorId(idCiclista).isPresent()) {
            return ResponseEntity.status(404).body(Map.of("erro", CICLISTA_NAO_ENCONTRADO));
        }
        Object bicicleta = service.getBicicletaAlugada(idCiclista);
        if (bicicleta == null) {
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.ok(bicicleta);
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
}