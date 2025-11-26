package com.example.aluguel_ms.ciclista.service;

import com.example.aluguel_ms.ciclista.model.Ciclista;
import com.example.aluguel_ms.ciclista.model.MeioDePagamento;
import com.example.aluguel_ms.ciclista.repository.CiclistaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CiclistaService {

    private final CiclistaRepository repository;

    public CiclistaService(CiclistaRepository repository) {
        this.repository = repository;
    }

    public boolean existeEmail(String email) {
        return repository.findByEmail(email).isPresent();
    }

    public Ciclista cadastrarCiclista(Ciclista ciclista, MeioDePagamento meioDePagamento) {
        ciclista.setMeioDePagamento(meioDePagamento);
        return repository.save(ciclista);
    }

        public Ciclista ativarCiclista(Integer id) {
            Optional<Ciclista> existente = repository.findById(id);
            if (existente.isPresent()) {
                Ciclista ciclista = existente.get();
                if ("pendente".equals(ciclista.getStatus())) {
                    ciclista.setStatus("ativo");
                    repository.save(ciclista);
                }
                return ciclista;
            }
            return null;
        }

    public List<Ciclista> listarTodos() {
        return repository.findAll();
    }

    public Optional<Ciclista> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public Ciclista atualizarCiclista(Integer id, Ciclista dadosAtualizados) {
        Optional<Ciclista> existente = repository.findById(id);
        if (existente.isPresent()) {
            Ciclista ciclista = existente.get();
            ciclista.setNome(dadosAtualizados.getNome());
            ciclista.setNascimento(dadosAtualizados.getNascimento());
            ciclista.setCpf(dadosAtualizados.getCpf());
            ciclista.setPassaporte(dadosAtualizados.getPassaporte());
            ciclista.setNacionalidade(dadosAtualizados.getNacionalidade());
            ciclista.setEmail(dadosAtualizados.getEmail());
            ciclista.setUrlFotoDocumento(dadosAtualizados.getUrlFotoDocumento());
            ciclista.setSenha(dadosAtualizados.getSenha());
            return repository.save(ciclista);
        }
        return null;
    }

    public boolean removerPorId(Integer id) {
        Optional<Ciclista> existente = repository.findById(id);
        if (existente.isPresent()) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}