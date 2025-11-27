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

    @PostMapping("/devolucao")
    public ResponseEntity<Object> devolverBicicleta(@RequestBody Map<String, Object> payload) {
        Integer idTranca = (Integer) payload.get("idTranca");
        Integer idBicicleta = (Integer) payload.get("idBicicleta");
        if (idTranca == null || idBicicleta == null) {
            return ResponseEntity.unprocessableEntity().body(java.util.List.of(new com.example.aluguel_ms.aluguel.model.Erro("Dados Inválidos")));
        }
        var result = aluguelService.devolverBicicleta(idTranca, idBicicleta);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.unprocessableEntity().body(java.util.List.of(new com.example.aluguel_ms.aluguel.model.Erro("Dados Inválidos")));
        }
    }

    @PostMapping
    public ResponseEntity<Object> alugarBicicleta(@RequestBody Map<String, Object> payload) {
        try {
            Integer ciclistaId = (Integer) payload.get("ciclista");
            Integer trancaId = (Integer) payload.get("trancaInicio");
            if (ciclistaId == null || trancaId == null) {
                return ResponseEntity.status(org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY).body(DADOS_INVALIDOS);
            }

            return aluguelService.alugarBicicleta(ciclistaId, trancaId)
                .map(aluguel -> ResponseEntity.status(org.springframework.http.HttpStatus.OK).body((Object) aluguel))
                .orElseGet(() -> ResponseEntity.status(org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY).body(DADOS_INVALIDOS));
        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(DADOS_INVALIDOS);
        }
    }
}
