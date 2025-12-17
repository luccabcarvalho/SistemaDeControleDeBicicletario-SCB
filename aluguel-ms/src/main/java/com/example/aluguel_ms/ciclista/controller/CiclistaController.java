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
                    boolean ativo = "CONFIRMADO".equals(ciclista.getStatus());
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
            return ResponseEntity.status(404).body(Map.of("erro", CICLISTA_NAO_ENCONTRADO));
        }
        if ("ativo".equals(ciclista.getStatus())) {
            return ResponseEntity.unprocessableEntity().body(Map.of("erro", "Ciclista já está ativo"));
        }
        if ("pendente".equals(ciclista.getStatus())) {
            Ciclista ativado = service.ativarCiclista(idCiclista);
            return ResponseEntity.ok(ativado);
        }
        return ResponseEntity.unprocessableEntity().body(Map.of("erro", "Não foi possível ativar o ciclista"));
    }

    @PostMapping
    public ResponseEntity<Object> cadastrarCiclista(@RequestBody Map<String, Object> payload) {
        if (payload == null || !payload.containsKey("ciclista") || !payload.containsKey("meioDePagamento")) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Requisição malformada: ciclista e meioDePagamento são obrigatórios"));
        }
        Map<String, Object> ciclistaMap;
        Map<String, Object> meioDePagamentoMap;
        try {
            ciclistaMap = (Map<String, Object>) payload.get("ciclista");
            meioDePagamentoMap = (Map<String, Object>) payload.get("meioDePagamento");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Estrutura do payload inválida"));
        }
        Ciclista ciclista;
        MeioDePagamento meioDePagamento;
        try {
            ciclista = Ciclista.fromMap(ciclistaMap);
            meioDePagamento = MeioDePagamento.fromMap(meioDePagamentoMap);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(Map.of("erro", "Dados inválidos para ciclista ou meio de pagamento"));
        }
        boolean cartaoValido = cartaoService.validarCartao(meioDePagamento);
        if (!cartaoValido) {
            return ResponseEntity.unprocessableEntity().body(Map.of("erro", "Cartão inválido"));
        }
        Ciclista criado = service.cadastrarCiclista(ciclista, meioDePagamento);
        String destinatario = criado.getEmail();
        String assunto = "Cadastro realizado com sucesso";
        String mensagem = "Olá, " + criado.getNome() + ", seu cadastro foi realizado com sucesso no sistema de bicicletário.";
        emailService.enviarEmail(destinatario, assunto, mensagem);
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
            .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @PutMapping("/{idCiclista}")
    public ResponseEntity<Ciclista> editarCiclista(@PathVariable Integer idCiclista, @RequestBody Map<String, Object> ciclistaMap) {
        Ciclista dadosAtualizados;
        try {
            dadosAtualizados = Ciclista.fromMap(ciclistaMap);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(null);
        }
        Ciclista atualizado = service.atualizarCiclista(idCiclista, dadosAtualizados);
        if (atualizado != null) {
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.status(404).body(null);
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