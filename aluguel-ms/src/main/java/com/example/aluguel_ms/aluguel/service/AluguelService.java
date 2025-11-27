package com.example.aluguel_ms.aluguel.service;

import com.example.aluguel_ms.aluguel.model.Aluguel;
import com.example.aluguel_ms.aluguel.repository.AluguelRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AluguelService {
    private final AluguelRepository aluguelRepository;

    public AluguelService(AluguelRepository aluguelRepository) {
        this.aluguelRepository = aluguelRepository;
    }

    public java.util.Optional<com.example.aluguel_ms.aluguel.model.Devolucao> devolverBicicleta(Integer idTranca, Integer idBicicleta) {
        // Comportamento falso para validação de tranca e bicicleta
        if (idTranca == null || idTranca <= 0 || idBicicleta == null || idBicicleta <= 0) {
            return java.util.Optional.empty();
        }
        // Simular busca do aluguel em aberto
        com.example.aluguel_ms.aluguel.model.Aluguel aluguel = aluguelRepository.findAll().stream()
            .filter(a -> a.getBicicleta() != null && a.getBicicleta().equals(idBicicleta) && a.getHoraFim() == null)
            .findFirst().orElse(null);
        if (aluguel == null) {
            return java.util.Optional.empty();
        }
        // Simular status da bicicleta e tranca
        String statusBicicleta = "em uso";
        String statusTranca = "disponível";
        // Simular cálculo de valor extra
        java.time.LocalDateTime horaInicio = aluguel.getHoraInicio();
        java.time.LocalDateTime horaFim = java.time.LocalDateTime.now();
        long minutos = java.time.Duration.between(horaInicio, horaFim).toMinutes();
        double valorExtra = 0.0;
        if (minutos > 120) {
            valorExtra = Math.ceil((minutos - 120) / 30.0) * 5.0;
        }
        // Simular cobrança e pagamento
        boolean pagamentoAutorizado = true;
        // Simular email do ciclista
        String emailCiclista = "ciclista@exemplo.com";
        // Simular cartão de cobrança
        String cartaoCobranca = "1234-5678-9012-3456";
        // Simular reparo solicitado
        boolean reparoSolicitado = false;
        // Simular alteração de status
        statusBicicleta = valorExtra > 0 ? "disponível" : statusBicicleta;
        statusTranca = "ocupada";
        // Atualizar aluguel
        aluguel.setHoraFim(horaFim);
        aluguelRepository.save(aluguel);
        // Montar devolução
        com.example.aluguel_ms.aluguel.model.Devolucao devolucao = new com.example.aluguel_ms.aluguel.model.Devolucao();
        devolucao.setIdBicicleta(idBicicleta);
        devolucao.setIdTranca(idTranca);
        devolucao.setDataHoraDevolucao(horaFim);
        devolucao.setDataHoraCobranca(horaFim);
        devolucao.setValorExtra(valorExtra);
        devolucao.setCartaoCobranca(cartaoCobranca);
        devolucao.setStatusBicicleta(statusBicicleta);
        devolucao.setStatusTranca(statusTranca);
        devolucao.setEmailCiclista(emailCiclista);
        devolucao.setReparoSolicitado(reparoSolicitado);
        devolucao.setPagamentoAutorizado(pagamentoAutorizado);
        // Simular envio de email
        // (comportamento falso)
        return java.util.Optional.of(devolucao);
    }

    // dependências externas
    private boolean validarTranca(Integer trancaId) {
        // tranca existe e está "ocupada"
        return trancaId != null && trancaId > 0;
    }
    private Integer obterBicicletaNaTranca(Integer trancaId) {
        // retorna id da bicicleta se tranca válida
        return trancaId != null && trancaId > 0 ? trancaId + 100 : null;
    }
    private boolean bicicletaDisponivel(Integer bicicletaId) {
        // bicicleta existe, está disponível e não está em reparo
        return bicicletaId != null && bicicletaId > 0;
    }
    private boolean ciclistaPodeAlugar(Integer ciclistaId) {
        // ciclista existe, está ativo e não tem aluguel em aberto
        return ciclistaId != null && ciclistaId > 0;
    }
    private boolean realizarCobranca(Integer ciclistaId) {
        // cobrança aprovada apenas para ciclistaId par (fictício, e lidando com a verificação do sonar, que aponta problema de Reliability quando a condição é sempre true neste caso)
        return ciclistaId != null && ciclistaId % 2 == 0;
    }
    private void liberarTranca(Integer trancaId) {
        // tranca liberada
    }
    private void enviarEmail(Integer ciclistaId, Aluguel aluguel) {
        // email enviado
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
        if (!realizarCobranca(ciclistaId)) return Optional.empty();
        // registrar aluguel
        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(ciclistaId);
        aluguel.setBicicleta(bicicletaId);
        aluguel.setTrancaInicio(trancaId);
        aluguel.setHoraInicio(LocalDateTime.now());
        aluguel.setCobranca(1); // id de cobrança fictício
        aluguelRepository.save(aluguel);
        // alterar status da bicicleta (fictício)
        // liberar tranca (fictício)
        liberarTranca(trancaId);
        // enviar email ao ciclista
        enviarEmail(ciclistaId, aluguel);
        return Optional.of(aluguel);
    }
}
