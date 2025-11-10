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

    @PutMapping("/{idFuncionario}")
    public ResponseEntity<?> editarFuncionario(@PathVariable String idFuncionario, @RequestBody Funcionario funcionario) {
        java.util.Optional<Funcionario> existente = service.buscarPorMatricula(idFuncionario);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        funcionario.setMatricula(idFuncionario);
        java.util.List<String> erros = validarFuncionario(funcionario, false);
        if (!erros.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(erros);
        }
        Funcionario atualizado = service.atualizarFuncionario(funcionario);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{idFuncionario}")
    public ResponseEntity<?> removerFuncionario(@PathVariable String idFuncionario) {
        boolean removido = service.removerPorMatricula(idFuncionario);
        if (!removido) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    private java.util.List<String> validarFuncionario(Funcionario funcionario, boolean inclusao) {
        java.util.List<String> erros = new java.util.ArrayList<>();
        if (funcionario.getNome() == null || funcionario.getNome().isEmpty()) erros.add("Nome obrigatório");
        if (funcionario.getEmail() == null || funcionario.getEmail().isEmpty()) erros.add("Email obrigatório");
        if (funcionario.getSenha() == null || funcionario.getSenha().isEmpty()) erros.add("Senha obrigatória");
        if (funcionario.getCpf() == null || funcionario.getCpf().isEmpty()) erros.add("CPF obrigatório");
        if (funcionario.getFuncao() == null || funcionario.getFuncao().isEmpty()) erros.add("Função obrigatória");
        else if (!funcionario.getFuncao().equalsIgnoreCase("administrativo") && !funcionario.getFuncao().equalsIgnoreCase("reparador")) erros.add("Função deve ser administrativo ou reparador");
        if (funcionario.getIdade() <= 0) erros.add("Idade obrigatória e maior que zero");
        if (!inclusao && funcionario.getMatricula() == null) erros.add("Matrícula obrigatória");
        return erros;
    }
}