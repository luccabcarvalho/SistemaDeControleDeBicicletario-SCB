package com.example.aluguel_ms.funcionario.service;

import com.example.aluguel_ms.funcionario.model.Funcionario;
import com.example.aluguel_ms.funcionario.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public Funcionario criarFuncionario(Funcionario funcionario) {
        String matricula;
        do {
            matricula = "F" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        } while (repository.findByMatricula(matricula).isPresent());
        funcionario.setMatricula(matricula);
        return repository.save(funcionario);
    }

    public List<Funcionario> listarTodos() {
        return repository.findAll();
    }

    public Optional<Funcionario> buscarPorMatricula(String matricula) {
        return repository.findByMatricula(matricula);
    }

    public Funcionario atualizarFuncionario(Funcionario funcionario) {
        return repository.save(funcionario);
    }

    public boolean removerPorMatricula(String matricula) {
        Optional<Funcionario> existente = repository.findByMatricula(matricula);
        if (existente.isPresent()) {
            repository.delete(existente.get());
            return true;
        }
        return false;
    }
}