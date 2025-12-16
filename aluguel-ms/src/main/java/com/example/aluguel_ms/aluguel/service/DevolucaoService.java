package com.example.aluguel_ms.aluguel.service;

import com.example.aluguel_ms.aluguel.model.Aluguel;
import com.example.aluguel_ms.aluguel.model.Devolucao;
import com.example.aluguel_ms.aluguel.repository.AluguelRepository;
import com.example.aluguel_ms.aluguel.repository.DevolucaoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class DevolucaoService {
    private final DevolucaoRepository devolucaoRepository;
    private final AluguelRepository aluguelRepository;
    private final WebClient webClient;

    @Value("${equipamento.url}")
    private String equipamentoUrl;

    @Value("${externo.url}")
    private String externoUrl;

    public DevolucaoService(DevolucaoRepository devolucaoRepository, AluguelRepository aluguelRepository, WebClient webClient) {
        this.devolucaoRepository = devolucaoRepository;
        this.aluguelRepository = aluguelRepository;
        this.webClient = webClient;
    }

    public Optional<Devolucao> processarDevolucao(Integer idTranca, Integer idBicicleta) {
        // 1. Buscar aluguel em aberto para a bicicleta
        Optional<Aluguel> aluguelOpt = aluguelRepository.findAll().stream()
                .filter(a -> a.getBicicleta().equals(idBicicleta) && a.getHoraFim() == null)
                .findFirst();
        if (aluguelOpt.isEmpty()) {
            return Optional.empty(); // Bicicleta não está alugada
        }
        Aluguel aluguel = aluguelOpt.get();

        // 2. Validar status da tranca e bicicleta
        if (!trancaDisponivel(idTranca) || !bicicletaEmUso(idBicicleta)) {
            return Optional.empty();
        }

        // 3. Calcular valor extra
        LocalDateTime agora = LocalDateTime.now();
        Duration tempoUso = Duration.between(aluguel.getHoraInicio(), agora);
        double valorExtra = calcularValorExtra(tempoUso);

        // 4. Realizar cobrança extra se necessário
        String statusPagamento = "ok";
        if (valorExtra > 0) {
            boolean pagamentoOk = realizarCobranca(aluguel.getCiclista(), valorExtra);
            if (!pagamentoOk) {
                statusPagamento = "pendente";
            }
        }

        // 5. Registrar devolução
        Devolucao devolucao = new Devolucao();
        devolucao.setAluguelId(aluguel.getId());
        devolucao.setBicicletaId(idBicicleta);
        devolucao.setTrancaId(idTranca);
        devolucao.setDataHoraDevolucao(agora);
        devolucao.setDataHoraCobranca(agora);
        devolucao.setValorExtra(valorExtra);
        devolucao.setCartao("cartao do ciclista"); // Buscar cartão real se necessário
        devolucao.setStatusPagamento(statusPagamento);
        devolucaoRepository.save(devolucao);

        // 6. Atualizar aluguel
        aluguel.setHoraFim(agora);
        aluguel.setTrancaFim(idTranca);
        aluguelRepository.save(aluguel);

        // 7. Atualizar status da bicicleta e tranca
        atualizarStatusBicicleta(idBicicleta, "disponivel");
        atualizarStatusTranca(idTranca, "ocupada");

        // 8. Enviar e-mail ao ciclista
        enviarEmailDevolucao(aluguel.getCiclista(), devolucao);

        return Optional.of(devolucao);
    }

    private boolean trancaDisponivel(Integer idTranca) {
        try {
            Map tranca = webClient.get()
                    .uri(equipamentoUrl + "/tranca/" + idTranca)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return tranca != null && "disponivel".equalsIgnoreCase((String) tranca.get("status"));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean bicicletaEmUso(Integer idBicicleta) {
        try {
            Map bicicleta = webClient.get()
                    .uri(equipamentoUrl + "/bicicleta/" + idBicicleta)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return bicicleta != null && "em uso".equalsIgnoreCase((String) bicicleta.get("status"));
        } catch (Exception e) {
            return false;
        }
    }

    private double calcularValorExtra(Duration tempoUso) {
        long minutos = tempoUso.toMinutes();
        if (minutos <= 120) return 0.0;
        long minutosExcedentes = minutos - 120;
        long meiaHoras = (minutosExcedentes + 29) / 30; // arredonda para cima
        return meiaHoras * 5.0;
    }

    private boolean realizarCobranca(Integer idCiclista, double valor) {
        try {
            Map<String, Object> payload = Map.of(
                    "ciclistaId", idCiclista,
                    "valor", valor
            );
            Boolean ok = webClient.post()
                    .uri(externoUrl + "/cobranca")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            return Boolean.TRUE.equals(ok);
        } catch (Exception e) {
            return false;
        }
    }

    private void atualizarStatusBicicleta(Integer idBicicleta, String status) {
        try {
            webClient.post()
                    .uri(equipamentoUrl + "/bicicleta/" + idBicicleta + "/status/" + status)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception ignored) {}
    }

    private void atualizarStatusTranca(Integer idTranca, String status) {
        try {
            webClient.post()
                    .uri(equipamentoUrl + "/tranca/" + idTranca + "/status/" + status)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception ignored) {}
    }

    private void enviarEmailDevolucao(Integer idCiclista, Devolucao devolucao) {
        try {
            // Buscar e-mail real do ciclista se necessário
            String destinatario = "ciclista@email.com";
            String assunto = "Devolução de bicicleta registrada";
            String mensagem = "Sua devolução foi registrada. Dados: " + devolucao.toString();
            webClient.post()
                    .uri(externoUrl + "/enviarEmail")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "destinatario", destinatario,
                            "assunto", assunto,
                            "mensagem", mensagem
                    ))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception ignored) {}
    }
}
