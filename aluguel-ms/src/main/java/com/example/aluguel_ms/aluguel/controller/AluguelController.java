package com.example.aluguel_ms.aluguel.controller;

import com.example.aluguel_ms.aluguel.model.Aluguel;
import com.example.aluguel_ms.aluguel.service.AluguelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/aluguel")
public class AluguelController {
    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    @PostMapping
    public ResponseEntity<?> alugarBicicleta(@RequestBody Map<String, Object> payload) {
        try {
            Integer ciclistaId = (Integer) payload.get("ciclista");
            Integer trancaId = (Integer) payload.get("trancaInicio");
            if (ciclistaId == null || trancaId == null) {
                return ResponseEntity.unprocessableEntity().body("Dados Inválidos");
            }

                return aluguelService.alugarBicicleta(ciclistaId, trancaId)
                    .map(aluguel -> ResponseEntity.ok((Object) aluguel))
                    .orElseGet(() -> ResponseEntity.unprocessableEntity().body("Dados Inválidos"));
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body("Dados Inválidos");
        }
    }
}
