package com.example.aluguel_ms.util;

import com.example.aluguel_ms.ciclista.model.Ciclista;
import com.example.aluguel_ms.ciclista.model.MeioDePagamento;
import com.example.aluguel_ms.ciclista.repository.CiclistaRepository;
import com.example.aluguel_ms.aluguel.model.Aluguel;
import com.example.aluguel_ms.aluguel.repository.AluguelRepository;
import com.example.aluguel_ms.funcionario.model.Funcionario;
import com.example.aluguel_ms.funcionario.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("")
public class UtilController {
    @Autowired
    private CiclistaRepository ciclistaRepository;
    @Autowired
    private AluguelRepository aluguelRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    /**
     * Endpoint utilitário para restaurar o banco de dados para o estado base dos testes.
     * GET /restaurarDados
     */
    @GetMapping("/restaurarDados")
    public ResponseEntity<?> restaurarDados() {
        // Limpar dados existentes
        aluguelRepository.deleteAll();
        ciclistaRepository.deleteAll();
        funcionarioRepository.deleteAll();

        // Inserir ciclistas base
        List<Ciclista> ciclistas = new ArrayList<>();
        ciclistas.add(criarCiclista(1, "CONFIRMADO", "Fulano Beltrano", "2021-05-02", "78804034009", "Brasileiro", "user@example.com", "ABC123"));
        ciclistas.add(criarCiclista(2, "AGUARDANDO_CONFIRMACAO", "Fulano Beltrano", "2021-05-02", "43943488039", "Brasileiro", "user2@example.com", "ABC123"));
        ciclistas.add(criarCiclista(3, "CONFIRMADO", "Fulano Beltrano", "2021-05-02", "10243164084", "Brasileiro", "user3@example.com", "ABC123"));
        ciclistas.add(criarCiclista(4, "CONFIRMADO", "Fulano Beltrano", "2021-05-02", "30880150017", "Brasileiro", "user4@example.com", "ABC123"));
        ciclistaRepository.saveAll(ciclistas);

        // Inserir alugueis base
        List<Aluguel> alugueis = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        alugueis.add(criarAluguel(null, 3, 3, 2, null, now, null, 1));
        alugueis.add(criarAluguel(null, 4, 5, 4, null, now.minusHours(2), null, 2));
        alugueis.add(criarAluguel(null, 3, 1, 1, 2, now.minusHours(2), now, 3));
        aluguelRepository.saveAll(alugueis);

        // Inserir funcionário base
        Funcionario funcionario = new Funcionario();
        funcionario.setMatricula("12345");
        funcionario.setNome("Beltrano");
        funcionario.setEmail("employee@example.com");
        funcionario.setSenha("123");
        funcionario.setCpf("99999999999");
        funcionario.setFuncao("Reparador");
        funcionario.setIdade(25);
        funcionarioRepository.save(funcionario);

        return ResponseEntity.ok(Map.of("mensagem", "Banco restaurado com sucesso"));
    }

    private Ciclista criarCiclista(Integer id, String status, String nome, String nascimento, String cpf, String nacionalidade, String email, String senha) {
        Ciclista c = new Ciclista();
        c.setStatus(status);
        c.setNome(nome);
        c.setNascimento(LocalDate.parse(nascimento));
        c.setCpf(cpf);
        c.setNacionalidade(nacionalidade);
        c.setEmail(email);
        c.setSenha(senha);
        c.setUrlFotoDocumento("https://fakeimg.pl/350x200/?text=Doc");
        MeioDePagamento mp = new MeioDePagamento();
        mp.setNomeTitular(nome);
        mp.setNumero("4012001037141112");
        mp.setValidade(java.time.YearMonth.parse("2022-12"));
        mp.setCvv("132");
        c.setMeioDePagamento(mp);
        return c;
    }

    private Aluguel criarAluguel(Integer id, Integer ciclista, Integer bicicleta, Integer trancaInicio, Integer trancaFim, LocalDateTime horaInicio, LocalDateTime horaFim, Integer cobranca) {
        Aluguel a = new Aluguel();
        a.setId(id);
        a.setCiclista(ciclista);
        a.setBicicleta(bicicleta);
        a.setTrancaInicio(trancaInicio);
        a.setTrancaFim(trancaFim);
        a.setHoraInicio(horaInicio);
        a.setHoraFim(horaFim);
        a.setCobranca(cobranca);
        return a;
    }
}