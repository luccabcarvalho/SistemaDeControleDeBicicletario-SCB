package com.example.aluguel_ms.devolucao.controller;

import com.example.aluguel_ms.aluguel.model.Devolucao;
import com.example.aluguel_ms.aluguel.service.DevolucaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/devolucao")
public class DevolucaoController {
    private static final String DADOS_INVALIDOS = "Dados Inválidos";
    private final DevolucaoService devolucaoService;

    public DevolucaoController(DevolucaoService devolucaoService) {
        this.devolucaoService = devolucaoService;
    }

    @PostMapping
    public ResponseEntity<Object> devolverBicicleta(@RequestBody Map<String, Object> payload) {
        Integer trancaId = (Integer) payload.get("trancaId");
        Integer bicicletaId = (Integer) payload.get("bicicletaId");
        if (trancaId == null || bicicletaId == null) {
            return ResponseEntity.badRequest().body(DADOS_INVALIDOS);
        }
        return devolucaoService.processarDevolucao(trancaId, bicicletaId)
            .map(devolucao -> (Object) devolucao)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.unprocessableEntity().body(DADOS_INVALIDOS));
    }
}
