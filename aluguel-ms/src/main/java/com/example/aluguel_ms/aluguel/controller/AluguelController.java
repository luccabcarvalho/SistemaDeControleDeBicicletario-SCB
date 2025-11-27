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
            Integer ciclistaId = (Integer) payload.get("ciclista");
            Integer trancaId = (Integer) payload.get("trancaInicio");
            if (ciclistaId == null || trancaId == null) {
                return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(DADOS_INVALIDOS);
            }

            return aluguelService.alugarBicicleta(ciclistaId, trancaId)
                .map(aluguel -> ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(aluguel))
                .orElseGet(() -> ResponseEntity.status(org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY).body(DADOS_INVALIDOS));
        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(DADOS_INVALIDOS);
        }
    }
}
