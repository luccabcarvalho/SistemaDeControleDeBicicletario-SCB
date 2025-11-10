package com.example.aluguel_ms.controller;

import com.example.aluguel_ms.model.Funcionario;
import com.example.aluguel_ms.service.FuncionarioService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

    @Autowired
    private FuncionarioService service;

    @PostMapping
    public ResponseEntity<Funcionario> criarFuncionario(@RequestBody Funcionario funcionario) {
        Funcionario criado = service.criarFuncionario(funcionario);
        return ResponseEntity.ok(criado);
    }

    @GetMapping
    public ResponseEntity<List<Funcionario>> listarFuncionarios() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{idFuncionario}")
    public ResponseEntity<Funcionario> buscarFuncionario(@PathVariable String idFuncionario) {
        return service.buscarPorMatricula(idFuncionario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}