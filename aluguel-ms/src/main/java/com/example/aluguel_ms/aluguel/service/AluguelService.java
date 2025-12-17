package com.example.aluguel_ms.aluguel.service;

import com.example.aluguel_ms.aluguel.model.Aluguel;
import com.example.aluguel_ms.aluguel.repository.AluguelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Map;

@Service
public class AluguelService {
    private final AluguelRepository aluguelRepository;

    private final WebClient webClient;

    @Value("${equipamento.url}")
    private String equipamentoUrl;

    @Value("${externo.url}")
    private String externoUrl;

    public AluguelService(AluguelRepository aluguelRepository, WebClient webClient) {
        this.aluguelRepository = aluguelRepository;
        this.webClient = webClient;
    }

    public java.util.Optional<com.example.aluguel_ms.aluguel.model.Devolucao> devolverBicicleta(Integer idTranca, Integer idBicicleta) {
        // Buscar aluguel em aberto para a bicicleta
        Optional<Aluguel> aluguelOpt = aluguelRepository.findAll().stream()
                .filter(a -> a.getBicicleta() != null && a.getBicicleta().equals(idBicicleta) && a.getHoraFim() == null)
                .findFirst();
        if (aluguelOpt.isEmpty()) {
            return Optional.empty(); // Bicicleta não está alugada
        }
        Aluguel aluguel = aluguelOpt.get();

        // Simular validação de tranca e bicicleta (mock nos testes)
        // Calcular valor extra
        LocalDateTime agora = LocalDateTime.now();
        long minutos = java.time.Duration.between(aluguel.getHoraInicio(), agora).toMinutes();
        double valorExtra = 0.0;
        if (minutos > 120) {
            long minutosExcedentes = minutos - 120;
            long meiaHoras = (minutosExcedentes + 29) / 30;
            valorExtra = meiaHoras * 5.0;
        }
        // Simular cobrança extra (mock nos testes)
        String statusPagamento = "ok";
        if (valorExtra > 0) {
            statusPagamento = "ok";
        }

        // Criar devolução
        com.example.aluguel_ms.aluguel.model.Devolucao devolucao = new com.example.aluguel_ms.aluguel.model.Devolucao();
        devolucao.setIdBicicleta(idBicicleta);
        devolucao.setIdTranca(idTranca);
        devolucao.setValorExtra(valorExtra);
        devolucao.setStatusTranca("ocupada");
        devolucao.setStatusPagamento(statusPagamento);

        // Atualizar aluguel
        aluguel.setHoraFim(agora);
        aluguel.setTrancaFim(idTranca);
        aluguelRepository.save(aluguel);

        return Optional.of(devolucao);
    }

    // dependências externas
    protected boolean validarTranca(Integer trancaId) {
        if (trancaId == null || trancaId <= 0) return false;
        try {
            String url = equipamentoUrl + "/tranca/" + trancaId;
            webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    protected Integer obterBicicletaNaTranca(Integer trancaId) {
        if (trancaId == null || trancaId <= 0) return null;
        try {
            String url = equipamentoUrl + "/tranca/" + trancaId + "/bicicleta";
            Map bicicleta = webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
            if (bicicleta != null && bicicleta.get("id") != null) {
                Object idObj = bicicleta.get("id");
                if (idObj instanceof Integer) return (Integer) idObj;
                if (idObj instanceof String) return Integer.valueOf((String) idObj);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    protected boolean bicicletaDisponivel(Integer bicicletaId) {
        if (bicicletaId == null || bicicletaId <= 0) return false;
        try {
            String url = equipamentoUrl + "/bicicleta/" + bicicletaId;
            Map bicicleta = webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
            if (bicicleta != null && "disponivel".equalsIgnoreCase((String) bicicleta.get("status"))
                && !"em reparo".equalsIgnoreCase((String) bicicleta.get("status"))) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    protected boolean ciclistaPodeAlugar(Integer ciclistaId) {
        if (ciclistaId == null || ciclistaId <= 0) return false;
        try {
            Boolean permite = webClient.get()
                    .uri("http://localhost:8080/ciclista/" + ciclistaId + "/permiteAluguel")
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            return Boolean.TRUE.equals(permite);
        } catch (Exception e) {
            return false;
        }
    }
    protected boolean realizarCobranca(Integer ciclistaId) {
        if (ciclistaId == null || ciclistaId <= 0) return false;
        try {
            String url = externoUrl + "/cobranca";
            Map<String, Object> payload = Map.of("ciclistaId", ciclistaId);
            Boolean aprovado = webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
            return Boolean.TRUE.equals(aprovado);
        } catch (Exception e) {
            return false;
        }
    }
    private void liberarTranca(Integer trancaId) {
        if (trancaId == null || trancaId <= 0) return;
        try {
            String url = equipamentoUrl + "/tranca/" + trancaId + "/destrancar";
            webClient.post()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        } catch (Exception e) {
        }
    }
    private void enviarEmail(Integer ciclistaId, Aluguel aluguel) {
        if (ciclistaId == null || aluguel == null) return;
        try {
            String url = externoUrl + "/enviarEmail";
            Map<String, Object> payload = Map.of(
                "destinatario", "ciclista" + ciclistaId + "@exemplo.com",
                "assunto", "Confirmação de aluguel de bicicleta",
                "mensagem", "Seu aluguel foi realizado com sucesso. ID: " + aluguel.getId()
            );
            webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        } catch (Exception e) {
        }
    }

    public Optional<Aluguel> alugarBicicleta(Integer ciclistaId, Integer trancaId) {
        // validar tranca
        if (!validarTranca(trancaId)) return Optional.empty();
        // obter bicicleta na tranca
        Integer bicicletaId = obterBicicletaNaTranca(trancaId);
        if (bicicletaId == null) return Optional.empty();
        // validar bicicleta
        if (!bicicletaDisponivel(bicicletaId)) return Optional.empty();
        // validar ciclista
        if (!ciclistaPodeAlugar(ciclistaId)) return Optional.empty();
        // realizar cobrança
        System.out.println("==============================//============================= TROPA DO MAL TA NA PISTA ==============================");
        if (!realizarCobranca(ciclistaId)) return Optional.empty();
        // registrar aluguel
        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(ciclistaId);
        aluguel.setBicicleta(bicicletaId);
        aluguel.setTrancaInicio(trancaId);
        aluguel.setHoraInicio(LocalDateTime.now());
        aluguel.setCobranca(1); // id de cobrança fictício
        aluguelRepository.save(aluguel);
        liberarTranca(trancaId);
        // enviar email ao ciclista
        enviarEmail(ciclistaId, aluguel);
        return Optional.of(aluguel);
    }
}
