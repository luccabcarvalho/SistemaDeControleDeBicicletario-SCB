package com.example.aluguel_ms.aluguel.controller;

import com.example.aluguel_ms.aluguel.model.Aluguel;
import com.example.aluguel_ms.aluguel.service.AluguelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/aluguel")
public class AluguelController {
    private static final String DADOS_INVALIDOS = "Dados Inválidos";
    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    @PostMapping
    public ResponseEntity<Object> alugarBicicleta(@RequestBody Map<String, Object> payload) {
        try {
            if (payload == null || !payload.containsKey("ciclista") || !payload.containsKey("trancaInicio")) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Requisição malformada: ciclista e trancaInicio são obrigatórios"));
            }
            Object ciclistaObj = payload.get("ciclista");
            Object trancaObj = payload.get("trancaInicio");
            Integer ciclistaId = null;
            Integer trancaId = null;
            try {
                ciclistaId = (ciclistaObj instanceof Integer) ? (Integer) ciclistaObj : Integer.parseInt(ciclistaObj.toString());
                trancaId = (trancaObj instanceof Integer) ? (Integer) trancaObj : Integer.parseInt(trancaObj.toString());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Parâmetros inválidos: ciclista e trancaInicio devem ser inteiros"));
            }
            if (ciclistaId == null || trancaId == null) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Parâmetros obrigatórios ausentes"));
            }

            return aluguelService.alugarBicicleta(ciclistaId, trancaId)
                .map(aluguel -> ResponseEntity.ok((Object) aluguel))
                .orElseGet(() -> ResponseEntity.unprocessableEntity().body(Map.of("erro", DADOS_INVALIDOS)));
        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", DADOS_INVALIDOS));
        }
    }

}
