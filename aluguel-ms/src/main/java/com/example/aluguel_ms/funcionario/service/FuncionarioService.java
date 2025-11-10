package com.example.aluguel_ms.service;

import com.example.aluguel_ms.model.Funcionario;
import com.example.aluguel_ms.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;

    public Funcionario criarFuncionario(Funcionario funcionario) {
        return repository.save(funcionario);
    }

    public List<Funcionario> listarTodos() {
        return repository.findAll();
    }

    public Optional<Funcionario> buscarPorMatricula(String matricula) {
        return repository.findByMatricula(matricula);
    }
}