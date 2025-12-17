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
        if (payload == null || !payload.containsKey("trancaId") || !payload.containsKey("bicicletaId")) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Requisição malformada: trancaId e bicicletaId são obrigatórios"));
        }
        Object trancaObj = payload.get("trancaId");
        Object bicicletaObj = payload.get("bicicletaId");
        Integer trancaId = null;
        Integer bicicletaId = null;
        try {
            trancaId = (trancaObj instanceof Integer) ? (Integer) trancaObj : Integer.parseInt(trancaObj.toString());
            bicicletaId = (bicicletaObj instanceof Integer) ? (Integer) bicicletaObj : Integer.parseInt(bicicletaObj.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Parâmetros inválidos: trancaId e bicicletaId devem ser inteiros"));
        }
        if (trancaId == null || bicicletaId == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Parâmetros obrigatórios ausentes"));
        }
        return devolucaoService.processarDevolucao(trancaId, bicicletaId)
            .map(devolucao -> ResponseEntity.ok((Object) devolucao))
            .orElseGet(() -> ResponseEntity.unprocessableEntity().body(Map.of("erro", DADOS_INVALIDOS)));
    }
}
