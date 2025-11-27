package com.example.aluguel_ms.devolucao.controller;

import com.example.aluguel_ms.aluguel.model.Devolucao;
import com.example.aluguel_ms.aluguel.model.Erro;
import com.example.aluguel_ms.aluguel.service.AluguelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/devolucao")
public class DevolucaoController {
    private static final String DADOS_INVALIDOS = "Dados Inválidos";
    private final AluguelService aluguelService;

    public DevolucaoController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    @PostMapping
    public ResponseEntity<Object> devolverBicicleta(@RequestBody Map<String, Object> payload) {
        Integer idTranca = (Integer) payload.get("idTranca");
        Integer idBicicleta = (Integer) payload.get("idBicicleta");
        if (idTranca == null || idBicicleta == null) {
            return ResponseEntity.unprocessableEntity().body(List.of(new Erro(DADOS_INVALIDOS)));
        }
        Optional<Devolucao> result = aluguelService.devolverBicicleta(idTranca, idBicicleta);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.unprocessableEntity().body(List.of(new Erro(DADOS_INVALIDOS)));
        }
    }
}
